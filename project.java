/**
* CS4222/CS5052: Project 1
* Group 46L
* Author: Shane Conway
* Student Id: 17170451
* Due Date: 29/03/2018 10:00am
*/


import javax.swing.*;
import java.io.*;  
import java.util.*;

public class project{

	static String userId;
	public static ArrayList<ArrayList<String>>  teams;
	public static ArrayList<ArrayList<Integer>> fixtures;	
	public static ArrayList<ArrayList<Integer>> results;
	public static int [][] leaderBoard;

/**
	* main() method 
	* calls the outsideApp() method.
	*/ 
	public static void main(String[] args){
		outsideApp();
	}

/**
    * outsideApp() method is called from the
	* main() method, no values are passed to 
	* it.  This method displays the options available to the not logged in user
    */ 
	public static void outsideApp(){
		String[] options = {"Login", "Register", "Exit"};
		
		int optionPicked = JOptionPane.showOptionDialog(null, "Please select an option","League Application",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);	
		
		
		if (optionPicked==0) {
			login();
		}else if(optionPicked==1){
			register();
		}else if(optionPicked==2){
			JOptionPane.showMessageDialog(null,"Exiting...");
			System.exit(0);
		}
	}

/**
    * login() method is called from the
	* outsideApp() method, no values are passed to 
	* it.  This method displays the User Interface for logging in.
	* Performs various validation checks on input.
    */ 
	public static void login(){
		int attempts=0;
		String[] fileItem;
		Boolean located=false;

		File adminFilePath = new File("admin.txt");

		if(adminFilePath.exists()){
			try{
				JPanel login = new JPanel();

				JLabel userLabel = new JLabel("Username:");
				JTextField userInput = new JTextField(25);

				JLabel passLabel = new JLabel("Password:");
				JTextField passInput = new JTextField(25); 

				login.add(userLabel);
				login.add(userInput);
				login.add(passLabel);
				login.add(passInput);

				while(attempts<3){	

					int selection = JOptionPane.showConfirmDialog(null,login, "Login",JOptionPane.OK_CANCEL_OPTION);

					if (selection == JOptionPane.OK_OPTION) {
						
						String username = (userInput.getText()).trim();
						String password = (passInput.getText()).trim();

						if (username.equals("") || password.equals("") ) {
							JOptionPane.showMessageDialog(null,"All fields must be filled in");
						}else{
							FileReader adminFile = new FileReader("admin.txt");
							Scanner inAdminFile = new Scanner(adminFile);
							while(inAdminFile.hasNext()){
								fileItem = inAdminFile.nextLine().split(",");

								if(fileItem[1].equalsIgnoreCase(username) && fileItem[2].equals(password)){
									userId=fileItem[0];
									located=true;
									break;
								}
							}

							inAdminFile.close();
							adminFile.close();

							if (located==true) {
								mainMenu();
							}else{
								attempts++;
								JOptionPane.showMessageDialog(null,"Incorrect! Attempt number "+attempts,"Invalid Details",0);
							}
						}

					}else if(selection == JOptionPane.OK_CANCEL_OPTION){
						outsideApp();
					}
					
				}

				if (attempts>=3) {
					JOptionPane.showMessageDialog(null,"You have been blocked and the app will now close");
					System.exit(0);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			JOptionPane.showMessageDialog(null,"Administrator file does not exist.\nTherefore Application cannot run.","File Missing",0);
			System.exit(0);
		}
	}

/**
    * register() method is called from the
	* outsideApp() method, no values are passed to 
	* it.  This method displays the User Interface for registering.
	* Performs various validation checks on input.
    */ 
	public static void register(){
		String[] fileItem;
		boolean issues = false;
		File adminFilePath = new File("admin.txt");
		
		if(adminFilePath.exists()) {
			
				JPanel register = new JPanel();

				JLabel userLabel = new JLabel("Username:");
				JTextField userInput = new JTextField(25);

				JLabel passLabel = new JLabel("Password:");
				JTextField passInput = new JTextField(25); 

				register.add(userLabel);
				register.add(userInput);
				register.add(passLabel);
				register.add(passInput);

				do{	
					int selection = JOptionPane.showConfirmDialog(null,register, "Register",JOptionPane.OK_CANCEL_OPTION);

					if (selection == JOptionPane.OK_OPTION) {
						
						String username = (userInput.getText()).trim();
						String password = (passInput.getText()).trim();

						if (username.equals("") || password.equals("") ) {
							JOptionPane.showMessageDialog(null,"All fields must be filled in");
							issues = true;
						}else if(nameCheck("admin.txt",username)==false) {
							issues = true;
							JOptionPane.showMessageDialog(null,"This username is already in use by another administrator");
						}else{
							issues = false;
							int id = Integer.parseInt(getLastLineInfo("admin.txt")[0])+1;
							String[] fileLine = {id+","+username+","+password};
							writeToAFile("admin.txt",fileLine,true);
							JOptionPane.showMessageDialog(null,"You have successfully registered. You can now log in.","Registration Success",1);
						}

					}else if(selection == JOptionPane.OK_CANCEL_OPTION){
						issues = false;
					}	
				}while(issues);

				outsideApp();

			
		}else{
			JOptionPane.showMessageDialog(null,"Administrator file does not exist.\nTherefore Application cannot run.","File Missing",0);
			System.exit(0);
		}	
	}

/**
    * mainMenu() method is called from the
	* login() method, no values are passed to 
	* it.  This method displays the options available to the user once they have logged in.
    */ 
	public static void mainMenu(){
		String[] options = {"View my Leagues", "Set up League", "Log out"};
		
		int optionPicked = JOptionPane.showOptionDialog(null, "Please select an option","Main Menu",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);	
		
		
		if (optionPicked==0) {
			viewLeagues();
		}else if(optionPicked==1){
			setupLeague();
		}else if(optionPicked==2){
			JOptionPane.showMessageDialog(null,"Logging out...");
			outsideApp();
		}	
	}

/**
    * viewLeagues() method is called from the
	* mainMenu() method, no values are passed to 
	* it. This method displays the leagues the user is an 
	* administrator of an allows the user to choose.
    */ 
	public static void viewLeagues(){
		//get leagues
		String leaguesFile = "leagues_"+userId+".txt";
		if (doesFileExist(leaguesFile)) {
			ArrayList<String> optionsArray = getLeagues(leaguesFile);
			String[] options = new String[optionsArray.size()];

			for(int i =0;i<optionsArray.size();i++){
				options[i] = optionsArray.get(i);
			}
			String selection = (String) JOptionPane.showInputDialog(null,"Select a league","My Leagues",1,null,options,options[0]);
			

			if (selection == null) {
				mainMenu();
			}else{
				showLeagueOptions(selection);
			}
		}else{
			JOptionPane.showMessageDialog(null,"You currently have no leagues setup");
			mainMenu();
		}
	}

/**
    * getLeagues() method is called from the
	* viewLeagues() method, a string of the league file is passed too it.
	* This method returns the leagues the user is an 
	* administrator of an allows viewLeagues to display them.
    */ 
	public static ArrayList<String> getLeagues(String leaguesFile){
		try{
			FileReader leagueFile = new FileReader(leaguesFile);
			Scanner in = new Scanner(leagueFile);

			ArrayList<String> leagues = new ArrayList<String>();


			String [] fileItem;

			while(in.hasNext()){
				fileItem = (in.nextLine()).split(",");
				leagues.add(fileItem[1]);
			}

			in.close();
			leagueFile.close();
			return leagues;

		}catch(IOException e){
			
			e.printStackTrace();
			return null;
		}
	}

/**
    * showLeagueOptions() method is called from the
	* viewLeagues() method, a string of the league name is passed too it.  
	* This method displays the options of the actions that can be carried out on the league.
    */ 
	public static void showLeagueOptions(String league){
		String [] leagueInfo = leagueInfo(league);
		int win = Integer.parseInt(leagueInfo[2]);
		int draw = Integer.parseInt(leagueInfo[3]);
		int loss = Integer.parseInt(leagueInfo[4]);
		String[] options = {"View league table", "View Fixtures","View Results","Enter Results","Edit Results","Delete league", "Main Menu"};
		
		int optionPicked = JOptionPane.showOptionDialog(null, "Please select an option","League Menu",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);	
		
		
		
		if (optionPicked==0) {
			viewLeagueTable(leagueInfo[0],leagueInfo[1],win,draw,loss);
		}else if(optionPicked==1){
			viewLeagueFixtures(leagueInfo[0],leagueInfo[1]);
		}else if(optionPicked==2){
			viewLeagueResults(leagueInfo[0],leagueInfo[1]);
		}else if(optionPicked==3){
			enterLeagueResults(leagueInfo[0],leagueInfo[1]);
		}else if(optionPicked==4){
			editLeagueResults(leagueInfo[0],leagueInfo[1]);
		}else if(optionPicked==5){
			deleteLeague(leagueInfo[0],leagueInfo[1]);
		}else if(optionPicked==6){
			mainMenu();
		}
	}

/**
    * leagueInfo() method is called from the
	* showLeagueOptions() method, a string of the league name is passed too it.  
	* This method returns the info for that league - points,name,league_id.
    */ 
	public static String [] leagueInfo(String league){
		try{
			FileReader leagueFile = new FileReader("leagues_"+userId+".txt");
			Scanner in = new Scanner(leagueFile);
			String [] fileItem;
			String [] infoOnLeague = new String[5];
			while(in.hasNext()){
				fileItem = in.nextLine().split(",");
				if (fileItem[1].equals(league)) {
					infoOnLeague[0]=fileItem[0];
					infoOnLeague[1]=fileItem[1];
					infoOnLeague[2]=fileItem[3];
					infoOnLeague[3]=fileItem[4];
					infoOnLeague[4]=fileItem[5];
					break;
				}
			}

			in.close();
			leagueFile.close();
			return infoOnLeague;
		}catch(IOException e){
			return null;
		}			
	}

/**
    * viewLeagueTable() method is called from the
	* showLeagueOptions() method, a string of the league id, name and points system is passed too it.  
	* This method displays the league table.
    */ 
	public static void viewLeagueTable(String leagueId,String league,int win,int draw,int loss){
		int readFile; 
	    readFile = readFilesIntoArrayLists(leagueId);
	    switch(readFile){
	    	case 1 : JOptionPane.showMessageDialog(null,"1 or more critical files are missing.","No Files Found",0);
					 showLeagueOptions(league);
	    			 break;
	    	case 2 : JOptionPane.showMessageDialog(null,"League table generated after results of first round entered","Results needed",0);
					 showLeagueOptions(league);
	    			 break;
	    	case 3 : createEmptyLeaderBoard();
				     processResults(win,draw,loss);
				     orderLeaderBoard();
				     displayLeaderboard(league);
				     showLeagueOptions(league);
	    			 break;
	    }
	}

/**
    * readFilesIntoArrayLists() method is called from the
	* viewLeagueTable() method, a string of the league id is passed too it.  
	* This method reads in the data needed to display the league table.
    */
	public static int readFilesIntoArrayLists(String leagueId){
		try{
		    String teamsFile = "teams_"+userId+"_"+leagueId+".txt";
			String fixturesFile = "fixtures_"+userId+"_"+leagueId+".txt";
			String resultsFile = "results_"+userId+"_"+leagueId+".txt";
		    
		    String fileElements[];
		  	File inputFile1 = new File(teamsFile);
		  	File inputFile2 = new File(fixturesFile);
		  	File inputFile3 = new File(resultsFile);
		  	
		  	teams = new ArrayList<ArrayList<String>>();
		  	teams.add(new ArrayList<String>());
		    teams.add(new ArrayList<String>());
		  
		    fixtures = new ArrayList<ArrayList<Integer>>();
			fixtures.add(new ArrayList<Integer>());
		    fixtures.add(new ArrayList<Integer>());
		    fixtures.add(new ArrayList<Integer>());
		    
		    results = new ArrayList<ArrayList<Integer>>();
			results.add(new ArrayList<Integer>());
		    results.add(new ArrayList<Integer>());
		    results.add(new ArrayList<Integer>());
		    
		    if (criticalFilesExist(leagueId)){
			  	if (inputFile3.exists()){
			  	  Scanner in;
			  	  in = new Scanner(inputFile1);
			  	  while(in.hasNext()){
			  	    fileElements = (in.nextLine()).split(",");
			  	    teams.get(0).add(fileElements[0]);  
			  	    teams.get(1).add(fileElements[1]);  
			  	  } 
			  	  in.close();

			  	  in = new Scanner(inputFile2);
			  	  while(in.hasNext()){
			  	    fileElements = (in.nextLine()).split(",");
			  	    fixtures.get(0).add(Integer.parseInt(fileElements[0]));  
			  	    fixtures.get(1).add(Integer.parseInt(fileElements[3]));  
			  	    fixtures.get(2).add(Integer.parseInt(fileElements[4]));  
			  	  } 
			  	  in.close();

			  	  in = new Scanner(inputFile3);
			  	  while(in.hasNext()){
			  	    fileElements = (in.nextLine()).split(",");
			  	    results.get(0).add(Integer.parseInt(fileElements[0]));  
			  	    results.get(1).add(Integer.parseInt(fileElements[3]));  
			  	    results.get(2).add(Integer.parseInt(fileElements[4]));  
			  	  } 
			  	  in.close();

			  	  return 3;//true

			    }else{
			       return 2;// no results
			    }
			}else{
			   return 1;
			}
		}catch(IOException e){
			e.printStackTrace();
			return 1;
		}
	}

/**
    * createEmptyLeaderBoard() method is called from the
	* viewLeagueTable() method, no value is passed too it.  
	* This method initailises the league table.
    */
	public static void createEmptyLeaderBoard(){
	  	// find out the number of teams/players which will determine 
	  	// the number of rows  
	    int rows = teams.get(0).size();
	  	int columns = 14;  
	  	leaderBoard = new int[rows][columns];
	  	// place team numbers in column 0 of leader board
	  	for (int i = 0; i < leaderBoard.length; i++){
	        leaderBoard[i][0] = Integer.parseInt(teams.get(0).get(i));
	  	}
	}	

/**
    * processResults() method is called from the
	* viewLeagueTable() method, the points system is passed too it.  
	* This method goes through each result and decides what is the correct course of action.
    */   
	public static void processResults(int win,int draw,int loss){
	  	int fixtureNumber, homeTeamScore, awayTeamScore, homeTeamNumber, awayTeamNumber;
	  	int position;
	  	for (int i = 0; i < results.get(0).size(); i++){
	  	  fixtureNumber  = results.get(0).get(i);
	  	  homeTeamScore  = results.get(1).get(i);
	  	  awayTeamScore  = results.get(2).get(i);
	  	  position       = fixtures.get(0).indexOf(fixtureNumber);
	  	  homeTeamNumber = fixtures.get(1).get(position);
	  	  awayTeamNumber = fixtures.get(2).get(position);
	  	  if (homeTeamNumber<=teams.get(0).size() && awayTeamNumber<=teams.get(0).size()) {
		  	  if (homeTeamScore == awayTeamScore){
		  		recordFixtureResultForHomeTeam(homeTeamNumber,0,1,0,homeTeamScore,awayTeamScore,draw);
		  		recordFixtureResultForAwayTeam(awayTeamNumber,0,1,0,homeTeamScore,awayTeamScore,draw);
		  	  }else if (homeTeamScore > awayTeamScore){
		  		recordFixtureResultForHomeTeam(homeTeamNumber,1,0,0,homeTeamScore,awayTeamScore,win);
		  		recordFixtureResultForAwayTeam(awayTeamNumber,0,0,1,homeTeamScore,awayTeamScore,loss);  
		  	  }else{
		  		recordFixtureResultForHomeTeam(homeTeamNumber,0,0,1,homeTeamScore,awayTeamScore,loss);
		  		recordFixtureResultForAwayTeam(awayTeamNumber,1,0,0,homeTeamScore,awayTeamScore,win);  
		  	  }  
		  }  
	    }
	}

/**
    * recordFixtureResultForHomeTeam() method is called from the
	* processResults() method, variety of values is passed too it.  
	* This method reads in the result of each match and keeps track of it.
    */ 
	public static void recordFixtureResultForHomeTeam(int hTN, int w, int d, int l, int hTS, int aTS, int p){
		leaderBoard[hTN-1][1]++;        			// gamesPlayed
		leaderBoard[hTN-1][2]+= w;      			// homeWin
		leaderBoard[hTN-1][3]+= d;      			// homeDraw
		leaderBoard[hTN-1][4]+= l;      			// homeLoss
		leaderBoard[hTN-1][5]+= hTS;    			// homeTeamScore
		leaderBoard[hTN-1][6]+= aTS;    			// awayTeamScore
		leaderBoard[hTN-1][12] += (hTS - aTS);    	// goalDifference
		leaderBoard[hTN-1][13] += p;    			// points
  	}

/**
    * recordFixtureResultForAwayTeam() method is called from the
	* processResults() method, variety of values is passed too it.  
	* This method reads in the result of each match and keeps track of it.
    */ 
  	public static void recordFixtureResultForAwayTeam(int aTN, int w, int d, int l, int hTS, int aTS, int p){
		leaderBoard[aTN-1][1]++;        			// gamesPlayed
		leaderBoard[aTN-1][7]+= w;      			// awayWin
		leaderBoard[aTN-1][8]+= d;      			// awayDraw
		leaderBoard[aTN-1][9]+= l;      			// awayLoss
		leaderBoard[aTN-1][10]+= aTS;    			// awayTeamScore
		leaderBoard[aTN-1][11]+= hTS;    			// homeTeamScore
		leaderBoard[aTN-1][12] += (aTS - hTS);    	// goalDifference
		leaderBoard[aTN-1][13] += p;    			// points  
	}	

/**
    * orderLeaderBoard() method is called from the
	* viewLeagueTable() method, no value is passed too it.  
	* This method orders the league table.
    */
	public static void orderLeaderBoard(){
	  	int [][] temp = new int[leaderBoard.length][leaderBoard[0].length];
	    boolean finished = false;
	    while (!finished){
	      finished = true;
	      for (int i = 0; i < leaderBoard.length - 1; i++){
	        if (leaderBoard[i][13] < leaderBoard[i + 1][13]){
	          for (int j = 0; j < leaderBoard[i].length; j++){
	            temp[i][j]            = leaderBoard[i][j];
	            leaderBoard[i][j]     = leaderBoard[i + 1][j];
	            leaderBoard[i + 1][j] = temp[i][j];
	          }
	          finished = false;
	        }
	      }
	    }
	}	  

/**
    * readFilesIntoArrayLists() method is called from the
	* viewLeagueTable() method, a string of the league name is passed too it.  
	* This method displays the league table.
    */	  
    public static void displayLeaderboard(String league){
	  int aTeamNumber;
	  String aTeamName, formatStringTeamName;
	  String longestTeamName = teams.get(1).get(0);
	  int longestTeamNameLength = longestTeamName.length();
      
      for (int i = 1; i < teams.get(1).size(); i++){
  	  longestTeamName = teams.get(1).get(i);  
        if (longestTeamNameLength < longestTeamName.length()){
          longestTeamNameLength = longestTeamName.length();
        }
      }
      if (longestTeamNameLength<9) {
      	longestTeamNameLength=9;
      }
     // formatStringTeamName = "%-" + (longestTeamNameLength + 2) + "s";
      // formatStringTeamName = "%" + (maxLength-aTeamName.length()) + "s";
      String displayLeagueTable="";
      displayLeagueTable+=String.format("%-"+(longestTeamNameLength-9+2)+"s","Team Name");
   	  displayLeagueTable+=String.format("  GP  HW  HD  HL  GF  GA  AW  AD  AL  GF  GA   GD   TP\n"); 
     
      for (int i = 0; i < leaderBoard.length; i++){
  	  		aTeamNumber = leaderBoard[i][0];
  	  		aTeamName   = teams.get(1).get(aTeamNumber - 1);       
	        displayLeagueTable+=String.format("%-"+(longestTeamNameLength-aTeamName.length())+"s", aTeamName)+
	        String.format("%5d", leaderBoard[i][1])+
	        String.format("%5d", leaderBoard[i][2])+
	        String.format("%5d", leaderBoard[i][3])+
	        String.format("%5d", leaderBoard[i][4])+
	        String.format("%5d", leaderBoard[i][5])+
	        String.format("%5d", leaderBoard[i][6])+
	        String.format("%5d", leaderBoard[i][7])+
	  	  	String.format("%5d", leaderBoard[i][8])+
	        String.format("%5d", leaderBoard[i][9])+
	        String.format("%5d", leaderBoard[i][10])+
	        String.format("%5d", leaderBoard[i][11])+
	        String.format("%6d", leaderBoard[i][12])+
	        String.format("%6d", leaderBoard[i][13])+"\n";
      }

      JOptionPane.showMessageDialog(null,displayLeagueTable,league,1);
    } 

/**
    * viewLeagueFixtures() method is called from the
	* showLeagueOptions() method, a string of the league id and name is passed too it.  
	* This method displays the league fixtures.
    */ 
	public static void viewLeagueFixtures(String leagueId,String league){
		String fixturesFile = "fixtures_"+userId+"_"+leagueId+".txt";
		if(criticalFilesExist(leagueId)){
			int numRounds=Integer.parseInt(getLastLineInfo(fixturesFile)[1]);
			String[] options = new String[numRounds];

			for(int i =0;i<numRounds;i++){
				options[i] = ""+(i+1);
			}
			String roundNum = (String) JOptionPane.showInputDialog(null,"Select a round: ","Fixtures for "+league,1,null,options,options[0]);
			

			if (roundNum == null) {
				showLeagueOptions(league);
			}else{
				ArrayList<String> roundOfFixtures = getARound(fixturesFile,roundNum);
				String displayOfFixtures="";
				String teamsFile = "teams_"+userId+"_"+leagueId+".txt";
				for (int j=0;j<roundOfFixtures.size();j++ ) {
					String[] fixture = roundOfFixtures.get(j).split(",");
					int numberOfTeams = Integer.parseInt(getLastLineInfo(teamsFile)[0]);
					int homeTeamNumber = Integer.parseInt(fixture[3]);
					int awayTeamNumber = Integer.parseInt(fixture[4]);
					String homeTeam = getTeam(leagueId,fixture[3]);
					String awayTeam = getTeam(leagueId,fixture[4]);
					if (homeTeamNumber>numberOfTeams) {
						displayOfFixtures+="Match "+fixture[2]+": "+awayTeam+" has a bye for this round.\n";
					}else if (awayTeamNumber>numberOfTeams) {
						displayOfFixtures+="Match "+fixture[2]+": "+homeTeam+" has a bye for this round.\n";
					}else{
						displayOfFixtures+="Match "+fixture[2]+": "+homeTeam+" v "+awayTeam+"\n";
					}
				}
				JOptionPane.showMessageDialog(null,displayOfFixtures);
				showLeagueOptions(league);
			}
		}else{
			JOptionPane.showMessageDialog(null,"1 or more critical files are missing.","No Files Found",0);
			showLeagueOptions(league);
		}
	}

/**
    * getTeam() method is called from the
	* viewLeagueFixtures() method, a string of the league id and team_id is passed too it.  
	* This method returns the team name for a given number.
    */ 
	public static String getTeam(String leagueId, String team){
		try{
			String file = "teams_"+userId+"_"+leagueId+".txt";
			FileReader teamFile = new FileReader(file);
			Scanner in = new Scanner(teamFile);

			String teamName="";


			String [] fileItem;
			while(in.hasNext()){
				fileItem = (in.nextLine()).split(",");

				if (fileItem[0].equals(team)) {
					teamName=fileItem[1];
				}
			}

			in.close();
			teamFile.close();

			return teamName;

		}catch(IOException e){
			
			e.printStackTrace();
			return null;
		}
	}

/**
    * getARound() method is called from the
	* viewLeagueFixtures() method, a string of the file and round number is passed too it.  
	* This method returns a round of fixtures.
    */ 
	public static ArrayList<String> getARound(String file,String roundNum){
		try{
			String newFile = file;
			FileReader readFile = new FileReader(newFile);
			Scanner in = new Scanner(readFile);

			ArrayList<String> round = new ArrayList<String>();


			String [] fileItem;

			while(in.hasNext()){
				fileItem = (in.nextLine()).split(",");
				if (fileItem[1].equals(roundNum)) {
					round.add(fileItem[0]+","+fileItem[1]+","+fileItem[2]+","+fileItem[3]+","+fileItem[4]);
				}
			}

			in.close();
			readFile.close();

			return round;

		}catch(IOException e){
			
			e.printStackTrace();
			return null;
		}
	}

/**
    * viewLeagueResults() method is called from the
	* showLeagueOptions() method, a string of the league id and name is passed too it.  
	* This method displays the league results.
    */ 
	public static void viewLeagueResults(String leagueId,String league){
		if(criticalFilesExist(leagueId)){
			String resultsFile = "results_"+userId+"_"+leagueId+".txt";
			if(doesFileExist(resultsFile)){
			
				int numRounds=Integer.parseInt(getLastLineInfo(resultsFile)[1]);
				String[] options = new String[numRounds];

				for(int i =0;i<numRounds;i++){
					options[i] = ""+(i+1);
				}
				String roundNum = (String) JOptionPane.showInputDialog(null,"Select a round: ","Results for "+league,1,null,options,options[0]);
				

				if (roundNum == null) {
					showLeagueOptions(league);
				}else{
					ArrayList<String> roundOfResults = getARound(resultsFile,roundNum);
					String displayOfResults="";
					for (int j=0;j<roundOfResults.size();j++ ) {
						String[] result = roundOfResults.get(j).split(",");
						String[] teams = getTeams(leagueId,result[0]);
						if (teams[0].length()==0) {
							displayOfResults+="Match "+result[2]+": "+teams[1]+" had a bye for this round.\n";
						}else if (teams[1].length()==0) {
							displayOfResults+="Match "+result[2]+": "+teams[0]+" had a bye for this round.\n";
						}else{
							displayOfResults+="Match "+result[2]+": "+teams[0]+" "+result[3]+" : "+result[4]+" "+teams[1]+"\n";
						}
					}
					JOptionPane.showMessageDialog(null,displayOfResults);
					showLeagueOptions(league);
				}
			}else{
				JOptionPane.showMessageDialog(null,"There are currently no results for this league");
				showLeagueOptions(league);
			}
		}else{
			JOptionPane.showMessageDialog(null,"1 or more critical files are missing.","No Files Found",0);
			showLeagueOptions(league);
		}
	}

/**
    * getTeams() method is called from the
	* viewLeagueResults() method, a string of the league id and match Number is passed too it.  
	* This method returns the team names from the match number.
    */
	public static String[] getTeams(String leagueId, String matchNum){
		try{
			String file = "fixtures_"+userId+"_"+leagueId+".txt";
			FileReader fixtureFile = new FileReader(file);
			Scanner in = new Scanner(fixtureFile);

			
			String homeTeam="";
			String awayTeam="";


			String [] fileItem;
			String [] teamItem;
			while(in.hasNext()){
				fileItem = (in.nextLine()).split(",");

				if (fileItem[0].equals(matchNum)) {
					String file1 = "teams_"+userId+"_"+leagueId+".txt";
					FileReader teamFile = new FileReader(file1);
					Scanner in2 = new Scanner(teamFile);
					while(in2.hasNext()){
						teamItem = (in2.nextLine()).split(",");
						if (teamItem[0].equals(fileItem[3])){
							homeTeam = teamItem[1];
						}

						if (teamItem[0].equals(fileItem[4])){
							awayTeam = teamItem[1];
						}
					}
				}
			}
			String[] teams = {homeTeam,awayTeam};

			in.close();
			fixtureFile.close();

			return teams;

		}catch(IOException e){
			
			e.printStackTrace();
			return null;
		}
	}

/**
    * enterLeagueResults() method is called from the
	* showLeagueOptions() method, a string of the league id and name is passed too it.  
	* This method allows you to enter the league results.
	* Performs various validation checks on input.
    */ 	
	public static void enterLeagueResults(String leagueId,String league){

		if(criticalFilesExist(leagueId)){
			String title="";
			String round="1";
			int matchID=0;
			boolean notRound1=true;


			String resultsFile = "results_"+userId+"_"+leagueId+".txt";
			if(doesFileExist(resultsFile)){
				String[] info = getLastLineInfo(resultsFile);
				round = Integer.toString(Integer.parseInt(info[1])+1);
				matchID=Integer.parseInt(info[0]);
				title="Please enter results for round "+round+" of the "+league+" league:\n";
			}else{
				title="Please enter results for round 1 of the "+league+" league:\n";
				notRound1=false;
			}

			String fixturesFile = "fixtures_"+userId+"_"+leagueId+".txt";
			ArrayList<String> roundOfFixtures = getARound(fixturesFile,round);
			String resultsDisplay="";
			String[] results = new String[roundOfFixtures.size()];
			String teamsFile = "teams_"+userId+"_"+leagueId+".txt";

			for (int j=0;j<roundOfFixtures.size();j++ ) {
				String[] fixture = roundOfFixtures.get(j).split(",");
				int numberOfTeams = Integer.parseInt(getLastLineInfo(teamsFile)[0]);
				int homeTeamNumber = Integer.parseInt(fixture[3]);
				int awayTeamNumber = Integer.parseInt(fixture[4]);
				String homeTeam = getTeam(leagueId,fixture[3]);
				String awayTeam = getTeam(leagueId,fixture[4]);
				if (homeTeamNumber>numberOfTeams) {
					results[j]=fixture[0]+","+fixture[1]+","+fixture[2]+",0,0";
					resultsDisplay="Match "+fixture[2]+": "+awayTeam+" has a bye for this round.\n";
					JOptionPane.showMessageDialog(null,resultsDisplay);
				}else if (awayTeamNumber>numberOfTeams) {
					results[j]=fixture[0]+","+fixture[1]+","+fixture[2]+",0,0";
					resultsDisplay="Match "+fixture[2]+": "+homeTeam+" has a bye for this round.\n";
					JOptionPane.showMessageDialog(null,resultsDisplay);
				}else{
					boolean issues = false;
					do{
						JPanel resultsPanel = new JPanel();

						JLabel homeLabel = new JLabel("Match "+fixture[2]+": "+homeTeam+":");
						JTextField homeTeamInput = new JTextField(25);
						JLabel vLabel = new JLabel(" v ");
						JLabel awayLabel = new JLabel(awayTeam+":");
						JTextField awayTeamInput = new JTextField(25); 

						resultsPanel.add(homeLabel);
						resultsPanel.add(homeTeamInput);
						resultsPanel.add(vLabel);
						resultsPanel.add(awayLabel);
						resultsPanel.add(awayTeamInput);
					
						int selection = JOptionPane.showConfirmDialog(null,resultsPanel, title,JOptionPane.OK_CANCEL_OPTION);

						if (selection == JOptionPane.OK_OPTION) {
							
							String homeTeamScore = (homeTeamInput.getText()).trim();
							String awayTeamScore = (awayTeamInput.getText()).trim();
							String pattern = "[0-9]+";
							if (homeTeamScore.equals("") || awayTeamScore.equals("") ) {
								JOptionPane.showMessageDialog(null,"All fields must be filled in");
								issues = true;
							}else if(!(homeTeamScore.matches(pattern)) || !(awayTeamScore.matches(pattern))){
								JOptionPane.showMessageDialog(null,"The values for the scores must be a non-negative whole number");
								issues = true;
							}else{
								results[j]=fixture[0]+","+fixture[1]+","+fixture[2]+","+homeTeamScore+","+awayTeamScore;
								issues = false;
							}
							

						}else if(selection == JOptionPane.OK_CANCEL_OPTION){
							showLeagueOptions(league);
						}
					}while(issues);
				}
			}

			writeToAFile(resultsFile,results,notRound1);
			showLeagueOptions(league);	
		}else{
			JOptionPane.showMessageDialog(null,"1 or more critical files are missing.","No Files Found",0);
			showLeagueOptions(league);
		}
	}

/**
    * editLeagueResults() method is called from the
	* showLeagueOptions() method, a string of the league id and name is passed too it.  
	* This method  allows you to edit individual league results.
	* Performs various validation checks on input.
    */ 
	public static void editLeagueResults(String leagueId,String league){
		if(criticalFilesExist(leagueId)){
			String resultsFile = "results_"+userId+"_"+leagueId+".txt";
			if(doesFileExist(resultsFile)){
				int numRounds=Integer.parseInt(getLastLineInfo(resultsFile)[1]);
				String[] options = new String[numRounds];

				for(int i =0;i<numRounds;i++){
					options[i] = ""+(i+1);
				}
				String roundNum = (String) JOptionPane.showInputDialog(null,"Select a round: ","Edit results for "+league,1,null,options,options[0]);
				

				if (roundNum == null) {
					showLeagueOptions(league);
				}else{

					int numMatches=Integer.parseInt(getLastLineInfo(resultsFile)[2]);
					String[] optionsMatches = new String[numMatches];

					for(int i =0;i<numMatches;i++){
						optionsMatches[i] = ""+(i+1);
					}
					String matchNum = (String) JOptionPane.showInputDialog(null,"Select a match: ","Edit results for "+league,1,null,optionsMatches,optionsMatches[0]);
					

					if (matchNum == null) {
						showLeagueOptions(league);
					}else{
						String[] matchResult = getAMatch(resultsFile,roundNum,matchNum);
						String displayOfResults="";
						String[] teams = getTeams(leagueId,matchResult[0]);
						if (teams[0].length()==0) {
							displayOfResults+="Match "+matchResult[2]+": "+teams[1]+" had a bye for this round. Therefore you cannot edit";
							JOptionPane.showMessageDialog(null,displayOfResults);
							showLeagueOptions(league);
						}else if (teams[1].length()==0) {
							displayOfResults+="Match "+matchResult[2]+": "+teams[0]+" had a bye for this round. Therefore you cannot edit";
							JOptionPane.showMessageDialog(null,displayOfResults);
							showLeagueOptions(league);
						}else{
							boolean issues = false;
							do{
								JPanel resultsPanel = new JPanel();
								JLabel oldResult = new JLabel("Round "+matchResult[1]+", Match "+matchResult[2]+": ("+teams[0]+" "+matchResult[3]+" : "+matchResult[4]+" "+teams[1]+")");
								JLabel homeLabel = new JLabel("=> Edit Result: "+teams[0]+":");
								JTextField homeTeamInput = new JTextField(25);
								JLabel vLabel = new JLabel(" v ");
								JLabel awayLabel = new JLabel(teams[1]+":");
								JTextField awayTeamInput = new JTextField(25); 

								resultsPanel.add(oldResult);
								resultsPanel.add(homeLabel);
								resultsPanel.add(homeTeamInput);
								resultsPanel.add(vLabel);
								resultsPanel.add(awayLabel);
								resultsPanel.add(awayTeamInput);
								
								String title="Please enter edited results:\n";
								int selection = JOptionPane.showConfirmDialog(null,resultsPanel, title,JOptionPane.OK_CANCEL_OPTION);

								if (selection == JOptionPane.OK_OPTION) {
									
									String homeTeamScore = (homeTeamInput.getText()).trim();
									String awayTeamScore = (awayTeamInput.getText()).trim();
									String pattern = "[0-9]+";
									if (homeTeamScore.equals("") || awayTeamScore.equals("") ) {
										JOptionPane.showMessageDialog(null,"All fields must be filled in");
										issues = true;
									}else if(!(homeTeamScore.matches(pattern)) || !(awayTeamScore.matches(pattern))){
											JOptionPane.showMessageDialog(null,"The values for the scores must be a non-negative whole number");
											issues = true;
									}else{
										String[] editedResult = {matchResult[0],matchResult[1],matchResult[2],homeTeamScore,awayTeamScore};
									
										if(changeLineInFile(resultsFile,editedResult)){
											JOptionPane.showMessageDialog(null,"Result edited successfully");
											showLeagueOptions(league);
										}else{
											JOptionPane.showMessageDialog(null,"Operation to edit result failed.");
											showLeagueOptions(league);
										}
										issues = false;
									}

								}else if(selection == JOptionPane.OK_CANCEL_OPTION){
									showLeagueOptions(league);
								}
							}while(issues);
						}	
					}
				}
			}else{
				JOptionPane.showMessageDialog(null,"No results have been entered to edit");
				showLeagueOptions(league);
			}
		}else{
			JOptionPane.showMessageDialog(null,"1 or more critical files are missing.","No Files Found",0);
			showLeagueOptions(league);
		}
	}

/**
    * getAMatch() method is called from the
	* editLeagueResults() method, a string of the file,round number and match number is passed too it.  
	* This method returns a the information on individual match fixture.
    */ 
	public static String[] getAMatch(String file,String roundNum,String matchNum){
		try{
			String newFile = file;
			FileReader readFile = new FileReader(newFile);
			Scanner in = new Scanner(readFile);

			String[] matchInfo = new String[5];


			String [] fileItem;

			while(in.hasNext()){
				fileItem = (in.nextLine()).split(",");
				if (fileItem[1].equals(roundNum)&&fileItem[2].equals(matchNum)) {
					matchInfo[0] = fileItem[0];
					matchInfo[1] = fileItem[1];
					matchInfo[2] = fileItem[2];
					matchInfo[3] = fileItem[3];
					matchInfo[4] = fileItem[4];
				}
			}

			in.close();
			readFile.close();

			return matchInfo;

		}catch(IOException e){
			
			e.printStackTrace();
			return null;
		}
	}

/**
    * deleteLeague() method is called from the
	* showLeagueOptions() method, a string of the league id and name is passed too it.  
	* This method processes the deletion of all files and information to do with league.
    */ 
	public static void deleteLeague(String leagueId,String league){
	
		String teamsFile = "teams_"+userId+"_"+leagueId+".txt";
		String fixturesFile = "fixtures_"+userId+"_"+leagueId+".txt";
		String resultsFile = "results_"+userId+"_"+leagueId+".txt";
		String[] files = {teamsFile,fixturesFile,resultsFile};

		String leaguesFile = "leagues_"+userId+".txt";
		

		if(deleteFiles(files)){
			if(deleteLineInFile(leaguesFile,leagueId)){
				JOptionPane.showMessageDialog(null,league+" has now been deleted");
				mainMenu();
			}else{
				JOptionPane.showMessageDialog(null,"Operation to update league file failed.");
				showLeagueOptions(league);
			}
		}else{
			JOptionPane.showMessageDialog(null,"Operation to delete files failed.");
			showLeagueOptions(league);
		}	
	}

/**
    * deleteLineInFile() method is called from the
	* deleteLeague() method, a string of the file and point where line is to be deleted is passed too it.  
	* This method deletes the line from the file.
    */ 
	public static boolean deleteLineInFile(String file,String pointOfEdit){
		File aFile = new File(file);
	    if (aFile.exists()){
	    	if (lineCount(file)<2) {
	    		if(aFile.delete()){
		     		return true;
		     	}else{
		    		return false;
		    	}
	    	}else{
	    		try{
					FileReader aFileReader = new FileReader(file);
					Scanner in = new Scanner(aFileReader);
					ArrayList<ArrayList<String>> leagueInfo = new ArrayList<ArrayList<String>>();

					leagueInfo.add(new ArrayList<String>());
					leagueInfo.add(new ArrayList<String>());
					leagueInfo.add(new ArrayList<String>());
					leagueInfo.add(new ArrayList<String>());
					leagueInfo.add(new ArrayList<String>());
					leagueInfo.add(new ArrayList<String>());

					while(in.hasNext()){
						String[] fileItem = (in.nextLine()).split(",");
						leagueInfo.get(0).add(fileItem[0]);
						leagueInfo.get(1).add(fileItem[1]);
						leagueInfo.get(2).add(fileItem[2]);
						leagueInfo.get(3).add(fileItem[3]);
						leagueInfo.get(4).add(fileItem[4]);
						leagueInfo.get(5).add(fileItem[5]);
					}

					in.close();
					aFileReader.close();


					int mark = leagueInfo.get(0).indexOf(pointOfEdit);
					leagueInfo.get(0).remove(mark);
					leagueInfo.get(1).remove(mark);
					leagueInfo.get(2).remove(mark);
					leagueInfo.get(3).remove(mark);
					leagueInfo.get(4).remove(mark);
					leagueInfo.get(5).remove(mark);
					
					reWriteAFile(file,leagueInfo);

					return true;
					
				}catch(IOException e){
					e.printStackTrace();
					return false;
				}

	    	}
	     	  
	    }else{
	    	return true;
	    }   	
	}

/**
    * changeLineInFile() method is called from the
	* editLeagueResults() method, a string of the file and array containing information to overwrite is passed too it.  
	* This method edits the line in the file.
    */ 
	public static boolean changeLineInFile(String file,String[] editedResult){
		File aFile = new File(file);
	    if (aFile.exists()){
	    	if (getLastLineInfo(file)[0].equals("1")) {
	    		if(aFile.delete()){
		     		return true;
		     	}else{
		    		return false;
		    	}
	    	}else{
	    		try{
					FileReader aFileReader = new FileReader(file);
					Scanner in = new Scanner(aFileReader);
					ArrayList<ArrayList<String>> resultInfo = new ArrayList<ArrayList<String>>();

					resultInfo.add(new ArrayList<String>());
					resultInfo.add(new ArrayList<String>());
					resultInfo.add(new ArrayList<String>());
					resultInfo.add(new ArrayList<String>());
					resultInfo.add(new ArrayList<String>());

					while(in.hasNext()){
						String[] fileItem = (in.nextLine()).split(",");
						resultInfo.get(0).add(fileItem[0]);
						resultInfo.get(1).add(fileItem[1]);
						resultInfo.get(2).add(fileItem[2]);
						resultInfo.get(3).add(fileItem[3]);
						resultInfo.get(4).add(fileItem[4]);
					}

					in.close();
					aFileReader.close();


					int mark = resultInfo.get(0).indexOf(editedResult[0]);
					resultInfo.get(3).set(mark,editedResult[3]);
					resultInfo.get(4).set(mark,editedResult[4]);
					
					reWriteAFile2(file,resultInfo);

					return true;
					
				}catch(IOException e){
					e.printStackTrace();
					return false;
				}

	    	}
	     	  
	    }else{
	    	return true;
	    }   	
	}

/**
    * deleteFiles() method is called from the
	* deleteLeague() method, an array of files to be deleted are passed too it.  
	* This method deletes the file.
    */ 
	public static boolean deleteFiles(String[] files){

		boolean[] deleted = {false,false,false};
	
		for (int i=0;i<files.length ;i++ ) {
			File aFile = new File(files[i]);
		    if (aFile.exists()){
		     	if(aFile.delete()){
		     		deleted[i]=true;
		     	}else{
		    		deleted[i]= false;
		    	}  
		    }else{
		    	deleted[i]= true;
		    }
		}

		if (deleted[0]==true && deleted[1]==true && deleted[2]==true) {
			return true;
		}else{
			return false;
		}
	}
	
/**
    * setupLeague() method is called from the
	* mainMenu() method, no values are passed to 
	* it. This method displays the user interface for setting up a league.
	* Various validation checks are carried out on the input.
    */ 
	public static void setupLeague(){
		boolean setupAchieved=false;
		JPanel setup = new JPanel();
		setup.setLayout(new BoxLayout(setup, BoxLayout.Y_AXIS));		

		JLabel nameLabel = new JLabel("League Name:");
		JTextField nameInput = new JTextField(25);

		JLabel fixtureLabel = new JLabel("How many times will each team play each other?");
		JTextField fixtureInput = new JTextField(3);

		JLabel winLabel = new JLabel("How many points awarded for a win?");
		JTextField winInput = new JTextField(3);

		JLabel drawLabel = new JLabel("How many points awarded for a draw?");
		JTextField drawInput = new JTextField(3);

		JLabel lossLabel = new JLabel("How many points awarded for a loss?");
		JTextField lossInput = new JTextField(3); 

		JLabel teamLabel = new JLabel("Add teams/players seperated by commas:");
		JTextField teamInput = new JTextField(15);


		setup.add(nameLabel);
		setup.add(nameInput);
		setup.add(fixtureLabel);
		setup.add(fixtureInput);
		setup.add(winLabel);
		setup.add(winInput);
		setup.add(drawLabel);
		setup.add(drawInput);
		setup.add(lossLabel);
		setup.add(lossInput);
		setup.add(teamLabel);
		setup.add(teamInput);

		do{

		int selection = JOptionPane.showConfirmDialog(null,setup, "Setup League",JOptionPane.OK_CANCEL_OPTION);

		if (selection == JOptionPane.OK_OPTION) {

			String name = (nameInput.getText()).trim();
			String fixtures = (fixtureInput.getText()).trim();
			String win = (winInput.getText()).trim();
			String draw = (drawInput.getText()).trim();
			String loss = (lossInput.getText()).trim();
			String teams = (teamInput.getText()).trim();
 
			String pattern = "[0-9]+";

			if (name.equals("") || fixtures.equals("") || win.equals("") || draw.equals("") || loss.equals("") || teams.equals("") ) {
				JOptionPane.showMessageDialog(null,"All fields must be filled in");
			}else{
				String[] teamsArray = teams.split(",");
				if (teamsArray.length<2) {
					JOptionPane.showMessageDialog(null,"You must insert at least two teams or players");
				}else if(wrongFormat(teamsArray)) {
					JOptionPane.showMessageDialog(null,"Incorrect format for team picks");
				}else if(name.length()>15) {
					JOptionPane.showMessageDialog(null,"League name cannot be more than 15 characters long");
				}else if(nameCheck("leagues_"+userId+".txt",name)==false) {
					JOptionPane.showMessageDialog(null,"You are an administrator of another League with this title. Please choose different title");
				}else if(!(fixtures.matches(pattern)) || !(win.matches(pattern)) || !(draw.matches(pattern)) || !(loss.matches(pattern))){
					JOptionPane.showMessageDialog(null,"The values for fixture/win/loss/draw must be a non-negative whole number");
				}else if(fixtures.equals("0") || win.equals("0") ){
					JOptionPane.showMessageDialog(null,"The values for fixture or win cannot be 0");
				}else{
					String leagues_Id = "leagues_"+userId+".txt";
					int leagueId=1;
					boolean append = false;

					

					if (doesFileExist(leagues_Id)) {
						leagueId =Integer.parseInt(getLastLineInfo(leagues_Id)[0])+1;
						append = true;
					}
					
					
					String[] leagueRow = {leagueId+","+name+","+fixtures+","+win+","+draw+","+loss};
					writeToAFile(leagues_Id,leagueRow, append);
					
					int numberOfTeams = teamsArray.length;
					String[] teamRow = new String[numberOfTeams];
					for(int i=0; i<teamsArray.length;i++){
						teamRow[i] = (i+1)+","+teamsArray[i];
					}
					String teamFile = "teams_"+userId+"_"+leagueId+".txt";
					String fixturesFile = "fixtures_"+userId+"_"+leagueId+".txt";
					writeToAFile(teamFile,teamRow,false);

					int numFixtures = Integer.parseInt(fixtures);
					String[] fixtureRow = generateFixtures(numFixtures,numberOfTeams);
					writeToAFile(fixturesFile,fixtureRow,false);
					JOptionPane.showMessageDialog(null,"League setup is successful");
					mainMenu();
				}
			}
		
			
		}else if(selection == JOptionPane.OK_CANCEL_OPTION){
			mainMenu();
		}
		}while(setupAchieved==false);
	}

/**
    * generateFixtures() method is called from the
	* setupLeague() method, the number of fixtures and the number of teams are passed to 
	* it. This method returns the fixtures for the league.
    */ 
	public static String[] generateFixtures(int numFixtures,int numberOfTeams){
		int aSetOfRounds, totalNumberOfRounds, numberOfMatchesPerRound;
	    int roundNumber, matchNumber, homeTeamNumber, awayTeamNumber, even, odd;
	    boolean additionalTeamIncluded = false;
	    String selection;
	    String [][] fixtures;
	    String [][] returnFixtures;
	    String [][] revisedFixtures;
	    String []   elementsOfFixture;
	    String fixtureAsText;
	    

       if (numberOfTeams % 2 == 1)
       {
	     numberOfTeams++;
	     additionalTeamIncluded = true;
       }
	   aSetOfRounds = numberOfTeams - 1;
       numberOfMatchesPerRound = numberOfTeams / 2;
       fixtures = new String[aSetOfRounds][numberOfMatchesPerRound];  
        
       for (roundNumber = 0; roundNumber < aSetOfRounds; roundNumber++) 
       {
         for (matchNumber = 0; matchNumber < numberOfMatchesPerRound; matchNumber++) 
	     {
           homeTeamNumber = (roundNumber + matchNumber) % (numberOfTeams - 1);
		   awayTeamNumber = (numberOfTeams - 1 - matchNumber + roundNumber) % (numberOfTeams - 1);
           if (matchNumber == 0) 
             awayTeamNumber = numberOfTeams - 1;
		   fixtures[roundNumber][matchNumber] = (homeTeamNumber + 1) + "," + (awayTeamNumber + 1);
         }
       } 
	   revisedFixtures = new String[aSetOfRounds][numberOfMatchesPerRound];
       even = 0;
       odd = numberOfTeams / 2;
       for (int i = 0; i < fixtures.length; i++) 
       {
         if (i % 2 == 0) 	
           revisedFixtures[i] = fixtures[even++];
         else 				
           revisedFixtures[i] = fixtures[odd++];
       }
       fixtures = revisedFixtures;
        

       //home then away
       for (roundNumber = 0; roundNumber < fixtures.length; roundNumber++) 
       {
         if (roundNumber % 2 == 1) 
	     {
	       fixtureAsText = fixtures[roundNumber][0];
	       elementsOfFixture = fixtureAsText.split(",");
           fixtures[roundNumber][0] = elementsOfFixture[1] + "," + elementsOfFixture[0];
	     }
       } 


       //mirror

       returnFixtures = new String[aSetOfRounds][numberOfMatchesPerRound];  
       for (roundNumber = 0; roundNumber < aSetOfRounds; roundNumber++) 
       {
         for (matchNumber = 0; matchNumber < numberOfMatchesPerRound; matchNumber++) 
	     {
		   fixtureAsText=fixtures[roundNumber][matchNumber];
		   elementsOfFixture = fixtureAsText.split(",");
           returnFixtures[roundNumber][matchNumber] = elementsOfFixture[1] + "," + elementsOfFixture[0];
         }
       } 


       //fix into string format to go into text file
       String[] fixtureRow = new String[aSetOfRounds*numberOfMatchesPerRound*numFixtures];
       int count = 0;
       int roundCount=0;

       for(int k=0;k<numFixtures;k++){
	       	if (k%2==0) {
	       		for (roundNumber = 0; roundNumber < aSetOfRounds; roundNumber++){
			        for (matchNumber = 0; matchNumber < numberOfMatchesPerRound; matchNumber++){
			         	fixtureRow[count] = (count + 1)+","+(roundCount + 1) +","+ (matchNumber + 1)+","+fixtures[roundNumber][matchNumber];
			        	count++;
			         }
			         roundCount++;
			    }
	       	}else{
	       		for (roundNumber = 0; roundNumber < aSetOfRounds; roundNumber++){
			        for (matchNumber = 0; matchNumber < numberOfMatchesPerRound; matchNumber++){
			         	fixtureRow[count] = (count + 1)+","+(roundCount + 1) +","+ (matchNumber + 1)+","+returnFixtures[roundNumber][matchNumber];
			        	count++;
			         }
			         roundCount++;
			    }
	       	}
       }

       return fixtureRow;
	}

	
	/**General Functions and are used in various methods*/

/**
    * wrongFormat() method is called from the
	* setupLeague() method, the teams entered are passed to 
	* it. This method returns wether teams were entered correctly.
    */ 
	public static boolean wrongFormat(String[] teamsArray){
		for (int i=0; i<teamsArray.length;i++ ) {
			if(teamsArray[i].length()==0){
				return true;
			}
		}
		return false;
	}

/**
	* doesFileExist() method => a string of the file is passed to 
	* it. This method returns wether the files exist.
    */
	public static boolean doesFileExist(String file){
			File fileTest = new File(file);

			if (fileTest.exists()) {
				return true;
			}else{
				return false;
			}
	}

/**
	* criticalFilesExist() method => a string of the league id is passed to 
	* it. This method returns wether the critical files exist.
    */
	public static boolean criticalFilesExist(String leagueId){
		String teamsFile = "teams_"+userId+"_"+leagueId+".txt";
		String fixturesFile = "fixtures_"+userId+"_"+leagueId+".txt";
		String leaguesFile = "leagues_"+userId+".txt";
		File teamsFileTest = new File(teamsFile);
		File fixturesFileTest = new File(fixturesFile);
		File leaguesFileTest = new File(leaguesFile);
		if (!(leaguesFileTest.exists())) {
				JOptionPane.showMessageDialog(null,"Administrator league file is missing.","No File Found",0);
				mainMenu();
				return false;
		}else{
			if (teamsFileTest.exists() && fixturesFileTest.exists() && leaguesFileTest.exists()) {
				return true;
			}else{
				return false;
			}
		}
	}

/**
    * reWriteAFile2() method is called from the
	* deleteLineInFile() method, the file and info to be rewritten are passed to 
	* it. This method rewrites the leagues file.
    */ 
	public static void reWriteAFile(String file,ArrayList<ArrayList<String>> fileInfo){

		try{
			FileWriter writeToFileWriter = new FileWriter(file,false);
			PrintWriter out = new PrintWriter(writeToFileWriter);
			for (int i=0; i<fileInfo.get(0).size(); i++) {
				out.println(
					fileInfo.get(0).get(i)+","+
					fileInfo.get(1).get(i)+","+
					fileInfo.get(2).get(i)+","+
					fileInfo.get(3).get(i)+","+
					fileInfo.get(4).get(i)+","+
					fileInfo.get(5).get(i)
				);
			}
			out.close();
			writeToFileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

/**
    * reWriteAFile2() method is called from the
	* changeLineInFile() method, the file and info to be rewritten are passed to 
	* it. This method rewrites the results file.
    */ 
	public static void reWriteAFile2(String file,ArrayList<ArrayList<String>> fileInfo){

		try{
			FileWriter writeToFileWriter = new FileWriter(file,false);
			PrintWriter out = new PrintWriter(writeToFileWriter);
			for (int i=0; i<fileInfo.get(0).size(); i++) {
				out.println(
					fileInfo.get(0).get(i)+","+
					fileInfo.get(1).get(i)+","+
					fileInfo.get(2).get(i)+","+
					fileInfo.get(3).get(i)+","+
					fileInfo.get(4).get(i)
				);
			}
			out.close();
			writeToFileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

/**
	* writeToAFile() method => a string of the file, the info to be written and wether it should overwrite is passed to 
	* it. 
    */
	public static void writeToAFile(String file,String[] fileLines,boolean append){

		try{
			FileWriter writeToFileWriter = new FileWriter(file,append);
			PrintWriter out = new PrintWriter(writeToFileWriter);
			for (int i=0; i<fileLines.length; i++) {
				out.println(fileLines[i]);
			}
			out.close();
			writeToFileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

/**
	* getLastLineInfo() method => a string of the file is passed to 
	* it. This method returns the information on the last line of the file.
    */
	public static String[] getLastLineInfo(String file){
		try{
			FileReader aFileReader = new FileReader(file);
			Scanner in = new Scanner(aFileReader);
			 String[] infoOnLastLine = new String[6];
			while(in.hasNext()){
				String[] fileItem = (in.nextLine()).split(",");
				
				for (int i=0;i<fileItem.length ;i++ ) {
					 infoOnLastLine[i]=fileItem[i];
				}
			}

			in.close();
			aFileReader.close();
			return infoOnLastLine;
			
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}	
	}

/**
	* lineCount() method => a string of the file is passed to 
	* it. This method returns the number of lines in a file.
    */
	public static int lineCount(String file){
		try{
			FileReader aFileReader = new FileReader(file);
			Scanner in = new Scanner(aFileReader);
			int lastLine = 0;
			while(in.hasNext()){
					lastLine++;
					in.nextLine();
			}

			in.close();
			aFileReader.close();

			return lastLine;
			
		}catch(IOException e){
			e.printStackTrace();
			return 0;
		}	
	}

/**
	* nameCheck() method => a string of the file and name to be checked are passed to 
	* it. This method returns wether the name exists or not.
    */
	public static boolean nameCheck(String file,String name){
		try{
			if(doesFileExist(file)){
				FileReader aFileReader = new FileReader(file);
				Scanner in = new Scanner(aFileReader);
				
				while(in.hasNext()){
					String[] fileItem = in.nextLine().split(",");
					if(fileItem[1].equals(name)){
						return false;
					}
				}
				in.close();
				aFileReader.close();
				return true;
			}else{
				return true;
			}	
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}

}