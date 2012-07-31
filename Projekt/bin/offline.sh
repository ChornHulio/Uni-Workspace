#!/bin/bash

# feature extraction settings
folder="offline/mfccSvm007"
speakerMin=1
speakerMax=10
extractionMethod="mfcc" # lpc or mfcc
sampleWidth=512
slidingRate=2
coefficents=20
window="hamming" # hamming or hann
energyLevel=90

# training settings
trainingMethod="svm" # neuralGas or svm
codebookSize=100
iterations=10
svmT=2 # 0=linear, 2=rbf
svmC=1
svmG=1

# create folder
if [ -d ../test/$folder ]
then
    rm -r ../test/$folder
fi
mkdir ../test/$folder

for j in {0..2}
do
	train1=$(( ( $j ) % 3 + 1))
	train2=$(( ( $j + 1) % 3 + 1))
	test=$(( ( $j + 2 ) % 3 + 1))

	# feature extraction
	echo "feature extraction ($train1)"
	for i in $(seq $speakerMin 1 $speakerMax)
	do
		# train features
		java -jar FeatureExtraction.jar -i ../speaker/$i/$train1.wav -i ../speaker/$i/$train2.wav -l $i -o ../test/$folder/features.train -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	
		# test features
		java -jar FeatureExtraction.jar -i ../speaker/$i/$test.wav -l $i -o ../test/$folder/features.test -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	
		# test features for analysis
		java -jar FeatureExtraction.jar -i ../speaker/$i/$test.wav -l $i -o ../test/$folder/features.analysis -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	done

if [ "$trainingMethod" = "svm" ]
then
	# svm scale
	echo "svm scale ($train1)"
	./svm-scale -s ../test/$folder/scale ../test/$folder/features.train > ../test/$folder/features.train.scale
	./svm-scale -r ../test/$folder/scale ../test/$folder/features.test > ../test/$folder/features.test.scale
	
	# svm training
	echo "svm train ($train1)"
	 ./svm-train -t $svmT -c $svmC -g $svmG -q ../test/$folder/features.train.scale ../test/$folder/model
	 
	# svm prediction
	echo "svm prediction ($train1)"
	./svm-predict ../test/$folder/features.test.scale ../test/$folder/model ../test/$folder/tmpResult
	
	# add tmpResult to result
	cat ../test/$folder/tmpResult >> ../test/$folder/result
	
	# remove unnecessary files
	rm ../test/$folder/features.train
	rm ../test/$folder/features.test
else
	# create codebook
	echo "create codebook ($train1)"
	java -jar CreateCodebook.jar -i ../test/$folder/features.train -o ../test/$folder/codebook -s $codebookSize -it $iterations

	# prediction
	echo "prediciton ($train1)"
	java -jar PredictByCodebook.jar -ff ../test/$folder/features.test -cf ../test/$folder/codebook -o ../test/$folder/result -a

	# remove unnecessary files
	rm ../test/$folder/features.train
	rm ../test/$folder/features.test
fi
done # end of outer for loop

# analysis
echo "analysis"
java -jar AnalyseResult.jar -i ../test/$folder/features.analysis -p ../test/$folder/result -o ../test/$folder/analysis
