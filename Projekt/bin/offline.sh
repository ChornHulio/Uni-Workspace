#!/bin/bash

# feature extraction settings
folder="mfccSvm001"
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
iterations=15
svmT=2 # 0=linear, 2=rbf
svmC=1
svmG=1

# create folder
if [ -d ../test/offline/$folder ]
then
    rm -r ../test/offline/$folder
fi
mkdir ../test/offline/$folder

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
		java -jar tdFeatureExtraction.jar -i ../speaker/$i/$train1.wav -i ../speaker/$i/$train2.wav -l $i -o ../test/offline/$folder/features.train -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	
		# test features
		java -jar tdFeatureExtraction.jar -i ../speaker/$i/$test.wav -l $i -o ../test/offline/$folder/features.test -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	
		# test features for analysis
		java -jar tdFeatureExtraction.jar -i ../speaker/$i/$test.wav -l $i -o ../test/offline/$folder/features.analysis -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	done

if [ "$trainingMethod" = "svm" ]
then
	# svm scale
	echo "svm scale ($train1)"
	./svm-scale -s ../test/offline/$folder/scale ../test/offline/$folder/features.train > ../test/offline/$folder/features.train.scale
	./svm-scale -r ../test/offline/$folder/scale ../test/offline/$folder/features.test > ../test/offline/$folder/features.test.scale
	
	# svm training
	echo "svm train ($train1)"
	 ./svm-train -t $svmT -c $svmC -g $svmG -q ../test/offline/$folder/features.train.scale ../test/offline/$folder/model
	 
	# svm prediction
	echo "svm prediction ($train1)"
	./svm-predict ../test/offline/$folder/features.test.scale ../test/offline/$folder/model ../test/offline/$folder/tmpResult
	
	# add tmpResult to result
	cat ../test/offline/$folder/tmpResult >> ../test/offline/$folder/result
	
	# remove unnecessary files
	rm ../test/offline/$folder/features.train
	rm ../test/offline/$folder/features.test
else
	# create codebook
	echo "create codebook ($train1)"
	java -jar tdCreateCodebook.jar -i ../test/offline/$folder/features.train -o ../test/offline/$folder/codebook -s $codebookSize -it $iterations

	# prediction
	echo "prediciton ($train1)"
	java -jar tdPredictByCodebook.jar -ff ../test/offline/$folder/features.test -cf ../test/offline/$folder/codebook -o ../test/offline/$folder/result -a

	# remove unnecessary files
	rm ../test/offline/$folder/features.train
	rm ../test/offline/$folder/features.test
	rm ../test/offline/$folder/codebook
fi
done # end of outer for loop

# analysis
echo "analysis"
java -jar tdAnalyseResult.jar -i ../test/offline/$folder/features.analysis -p ../test/offline/$folder/result -o ../test/offline/$folder/analysis
