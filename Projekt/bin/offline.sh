#!/bin/bash

# feature extraction settings
folder="./test"
folderWithSpeakers="../speaker/" # set this!!!
speakerMin=1
speakerMax=10
extractionMethod="mfcc" # lpc or mfcc
sampleWidth=512
slidingRate=2
coefficents=20
window="hamming" # hamming or hann
energyLevel=90

# training settings
trainingMethod="neuralGas" # 'neuralGas' or 'svm'
codebookSize=100
iterations=10
svmT=2 # 0=linear, 2=rbf
svmC=1
svmG=1

# create folder
if [ -d $folder ]
then
    rm -r $folder
fi
mkdir $folder

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
		java -jar FeatureExtraction.jar -i $folderWithSpeakers$i/$train1.wav -i $folderWithSpeakers$i/$train2.wav -l $i -o $folder/features.train -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	
		# test features
		java -jar FeatureExtraction.jar -i $folderWithSpeakers$i/$test.wav -l $i -o $folder/features.test -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	
		# test features for analysis
		java -jar FeatureExtraction.jar -i $folderWithSpeakers$i/$test.wav -l $i -o $folder/features.analysis -a --$extractionMethod -sw $sampleWidth -sr $slidingRate -c $coefficents -w $window -e $energyLevel
	done

if [ "$trainingMethod" = "svm" ]
then
	# svm scale
	echo "svm scale ($train1)"
	./svm-scale -s $folder/scale $folder/features.train > $folder/features.train.scale
	./svm-scale -r $folder/scale $folder/features.test > $folder/features.test.scale
	
	# svm training
	echo "svm train ($train1)"
	 ./svm-train -t $svmT -c $svmC -g $svmG -q $folder/features.train.scale $folder/model
	 
	# svm prediction
	echo "svm prediction ($train1)"
	./svm-predict $folder/features.test.scale $folder/model $folder/tmpResult
	
	# add tmpResult to result
	cat $folder/tmpResult >> $folder/result
	
	# remove unnecessary files
	rm $folder/features.train
	rm $folder/features.test
else
	# create codebook
	echo "create codebook ($train1)"
	java -jar CreateCodebook.jar -i $folder/features.train -o $folder/codebook -s $codebookSize -it $iterations

	# prediction
	echo "prediciton ($train1)"
	java -jar NearestNeighbor.jar -ff $folder/features.test -cf $folder/codebook -o $folder/result -a

	# remove unnecessary files
	rm $folder/features.train
	rm $folder/features.test
fi
done # end of outer for loop

# analysis
echo "analysis"
java -jar Analysis.jar -i $folder/features.analysis -p $folder/result -o $folder/analysis
