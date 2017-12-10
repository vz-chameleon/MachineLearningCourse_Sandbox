package cross_validation;

import java.util.ArrayList;

import data.Dataset;
import data.Sample;

public class KFold {
	private int k;
	private ArrayList<Dataset> kDatasets;
	
	public KFold(int k, Dataset originalDataset, boolean isRandomized) {
		this.k=k;
		this.kDatasets=new ArrayList<>();
		Dataset randomizedDataset = new Dataset(originalDataset);

		if (!isRandomized)
			randomizedDataset.shuffle();
		
		int datasetSize = randomizedDataset.getSize();
		
		int eachFoldSize = datasetSize/k;
		
		int leftOutDataSize = datasetSize-k*eachFoldSize;
		
		
		int foldStartIndex = 0;
		do {
			int foldEndIndex = foldStartIndex+eachFoldSize;
			
			if (leftOutDataSize>0) { // adding one sample to this fold if there is some left out data
				foldEndIndex++;
				leftOutDataSize--;
			}
			
			Dataset dset = new Dataset();
			for (Sample sample : randomizedDataset.getData().subList(foldStartIndex, foldEndIndex))
					dset.addEntry(sample);
			
			kDatasets.add(dset);
			foldStartIndex = foldEndIndex;
			
		}while (foldStartIndex<datasetSize);
		
	}
	
	public KFold(ArrayList<Dataset> kfolds) {
		this.k=kfolds.size();
		kDatasets = new ArrayList<>(kfolds);		
	}
	
	public int getNumberOfFolds() {
		return k;
	}
	
	public Dataset getFold(int i) {
		return kDatasets.get(i);
	}
	
	public ArrayList<Dataset> getOtherFolds(int i){
		ArrayList<Dataset> otherFolds =  new ArrayList<>(kDatasets.subList(0, i));
		otherFolds.addAll(kDatasets.subList(i+1, kDatasets.size()));
		return otherFolds;
	}	

}
