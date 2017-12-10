package nested_cross_validation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class holds the different values each parameter can be, in the form of one ArrayList<Double> per parameter
 * @author photon
 *
 */
public class ParameterSpace {
	HashMap<String,ArrayList<Double>> possibleParameterValues;



	public ParameterSpace() {
		possibleParameterValues = new HashMap<>();
	}

	public void addParameterAndValues(String aParameter, ArrayList<Double> aListOfPossibleValues) {
		possibleParameterValues.put(aParameter,aListOfPossibleValues);
	}



	public ArrayList<ArrayList<Double>> createEveryParameterCombination(){
		ArrayList<ArrayList<Double>> combination=new ArrayList<>();


		for (ArrayList<Double> paramValues : possibleParameterValues.values())
			combineListWithListOfList(combination, paramValues);

		return combination;

	}

	private void combineListWithListOfList(ArrayList<ArrayList<Double>> alist1, ArrayList<Double> list2){ 

		if (alist1.isEmpty())
		{
			for(Double d2: list2)
			{
				ArrayList<Double> al2 = new ArrayList<Double>();
				al2.add(d2);
				alist1.add(al2);
			}		
		}
		else {
			ArrayList<ArrayList<Double>> alist1_new = new ArrayList<>();
			for(ArrayList<Double> ad1 : alist1)
			{
				for(Double d2: list2)
				{
					ArrayList<Double> ad1d2 = new ArrayList<>(ad1);
					ad1d2.add(d2);
					alist1_new.add(ad1d2);
				}
			}
			alist1=alist1_new;
		}

	}
}
