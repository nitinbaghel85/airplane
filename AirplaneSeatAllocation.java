package com.nitin.airplane.service;

public class AirplaneSeatAllocation {
	private int seatsArray[][];
	private int noOfPassengers;
	private int seatAllocation[][][];

	public AirplaneSeatAllocation(int[][] seatsArray, int NoOfPassengers) {
		this.seatsArray = seatsArray;
		this.noOfPassengers = NoOfPassengers;
	}

	private int findMaxColumnLength() {
		int maxColumnLength = 0;
		for (int index = 0; index < seatsArray.length; index++) {
			if (maxColumnLength < seatsArray[index][1]) {
				maxColumnLength = seatsArray[index][1];
			}
		}
		return maxColumnLength;
	}

	private void fillSeats(int index1, int index2, int index3, int seatNumber) {
		//System.out.println("fillseats=" + index1 + "" + index2 + "" + index3 + "" + seatNumber);
		seatAllocation[index1][index2][index3] = seatNumber;

	}

	private void validateData() throws Exception {
		if (noOfPassengers == 0) {
			throw new Exception("No Of Passengers are 0");
		}
		if (seatsArray == null) {
			throw new Exception("SeatsArray is null");
		}
		int counter = 0;
		for (int[] seatArray : seatsArray) {

			if (seatArray == null) {
				throw new Exception("SeatsArray is null  at index [" + counter + "]");
			} else if (seatArray.length == 0) {
				throw new Exception("SeatsArray is blank  at index [" + counter + "]");
			} else if (seatArray[0] == 0) {
				throw new Exception("SeatsArray has blank values at index [" + counter + "][0]");
			} else if (seatArray[1] == 0) {
				throw new Exception("SeatsArray has blank values at index [" + counter + "][1]");
			}
			counter++;

		}
	}

	public void allocateSeats() throws Exception {

		validateData();
		// creating 3D array for seats allocation
		//System.out.println("creating 3D array for seats allocation");
		seatAllocation = new int[seatsArray.length][][];
		int counter = 0;
		for (int[] seatArray : seatsArray) {
			seatAllocation[counter++] = new int[seatArray[0]][seatArray[1]];
		}
		// created 3D array for seats allocation
		//System.out.println("created 3D array for seats allocation");
		int maxColumns = 0;
		int seatNumber = 1;
		maxColumns = findMaxColumnLength();
		// fill aisle seats exluding window seats
		// sample
		// [[3,2],[4,3],[2,3],[3,4]]
		// 020 100 130 200 210 300
		// 021 101 131 201 211 301
		// 102 132 202 212 302
		// 303
		//System.out.println("filling aisle seats exluding window seats");
		outer: for (int maxColIndex = 0; maxColIndex < maxColumns; maxColIndex++) {
			for (int ix = 0; ix < seatsArray.length; ix++) {

				try {
					if (ix == 0 && seatsArray[ix][0] != 1) {
						if (isPassengerAvaialable(seatNumber)) {
							fillSeats(ix, seatsArray[ix][0] - 1, maxColIndex, seatNumber);
							seatNumber++;
						} else {
							break outer;
						}

					} else if (ix == seatsArray.length - 1 && seatsArray[ix][0] != 1) {
						// System.out.println(seatNumber);

						if (isPassengerAvaialable(seatNumber)) {
							fillSeats(ix, 0, maxColIndex, seatNumber);
							seatNumber++;
						} else {
							break outer;
						}

					} else if (ix != 0 && ix != seatsArray.length - 1) {

						
						if (isPassengerAvaialable(seatNumber)) {
							fillSeats(ix, 0, maxColIndex, seatNumber);
							seatNumber++;
						} else {
							break outer;
						}

						if (seatsArray[ix][0] - 1 != 0) {
							if (isPassengerAvaialable(seatNumber)) {
								fillSeats(ix, seatsArray[ix][0] - 1, maxColIndex, seatNumber);
								seatNumber++;
							} else {
								break outer;
							}
						}
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					// System.out.println(e);
					continue;
				}

			}
		}
		//System.out.println("filled aisle seats exluding window seats");
		// fill window seats
		// sample
		// [[3,2],[4,3],[2,3],[3,4]]
		// 000 300 001 301 302 303
		//System.out.println("filling window seats");
		for (int maxColIndex = 0; maxColIndex < maxColumns; maxColIndex++) {

			try {
				if (isPassengerAvaialable(seatNumber)) {
					fillSeats(0, 0, maxColIndex, seatNumber);
					seatNumber++;
				} else {
					break ;
				}
				
			} catch (ArrayIndexOutOfBoundsException e) {
				// System.out.println(e);
				//seatNumber--;
			}
			try {
				//seatNumber++;
				
				
				if (isPassengerAvaialable(seatNumber)) {
					fillSeats(seatAllocation.length - 1, seatAllocation[seatAllocation.length - 1].length - 1, maxColIndex,
							seatNumber);
					seatNumber++;
				} else {
					break ;
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				// System.out.println(e);
				continue;
			}
			//seatNumber++;
		}
		//System.out.println("Filled window seats");
		// fill middle seats
		// sample
		// [[3,2],[4,3],[2,3],[3,4]]
		// 010 110 120 310
		// 011 111 121 311
		// 112 122 312
		// 313
		//System.out.println("filling middle seats");
		outer: for (int maxColIndex = 0; maxColIndex < maxColumns; maxColIndex++) {
			for (int ix = 0; ix < seatsArray.length; ix++) {

				try {
					for (int ixx = 1; ixx < seatsArray[ix][0] - 1; ixx++) {
						// System.out.println("index=" + ix + "" + ixx + "" + maxColIndex);
						if (isPassengerAvaialable(seatNumber)) {
							fillSeats(ix, ixx, maxColIndex, seatNumber);
							seatNumber++;
						} else {
							break outer;
						}
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					// System.out.println(e);
					continue;
				}
			}
		}
		//System.out.println("filled middle seats");

	}

	public String getSeatsAllocation() {
		StringBuffer seatsAllocation = new StringBuffer();

		for (int col[][] : seatAllocation) {
			for (int col1[] : col) {
				for (int col2 : col1) {
					seatsAllocation.append(col2+"-");
					// System.out.println(col2);
				}

			}
		}
		return seatsAllocation.toString();

	}

	private boolean isPassengerAvaialable(int seatNumber) {
		return noOfPassengers >= seatNumber;
	}

}
