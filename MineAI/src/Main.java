import java.util.*;

import mineSweeper.*;

public class Main {
	public static void main(String[] args) {

		int boardNumber,mapSize,coordinateX, coordinateY, isMine;
		String command;
		Oracle oracle;
		Scanner input = new Scanner(System.in);

		//select board and set map
		System.out.println("Select a board: 1(8x8) / 2(8x8) / 3(16x16) / 4(16x16) / 5(32x32)");
		System.out.print("> ");
		boardNumber = input.nextInt();
		
		//get map size
		System.out.println("Enter the map size: 0(8x8) / 1(16x16) / 2(32x32)");
		System.out.print("> ");
		mapSize = input.nextInt();
		
		oracle = new Oracle(mapSize);
		oracle.setMap("data/Board "+boardNumber+".txt");

		//start mine sweeper
		System.out.println("Enter a command: a(auto) / o(open) / c(current map) / s(score) / r(restart) / q(quit) / rs(restart samemap)");
		System.out.print("> ");
		command = input.next();

		while (!command.equals("q")) {
			if (command.equals("a")) {
				int[] mapSizeArr = {8,16,32};
				BoardMap map = new BoardMap(mapSizeArr[mapSize]);
				int X = (int)(Math.random()*mapSizeArr[mapSize]);
				int Y = (int)(Math.random()*mapSizeArr[mapSize]);

				ArrayList<Coordinate> list = oracle.actionPerform(X,Y,0);
				//ArrayList<Coordinate> list = oracle.actionPerform(2,1,0);
				oracle = autoMine(oracle,map,list);
				System.out.println("Remained Mines:"+oracle.getRemainedMines());
				map.getResult();
				//oracle.currentStatus();
				oracle.printScore();
			}
			else if (command.equals("o")) {
				System.out.print("X:");
				coordinateX = input.nextInt();
				System.out.print("Y:");
				coordinateY = input.nextInt();
				System.out.print("isMine (0-open tile / 1-remove mine):");
				isMine = input.nextInt();

				//coordinate open  -> oracle.actionPerform(x,y,0)
				//mine remove -> oracle.actionPerform(x,y,1)
				oracle.actionPerform(coordinateX, coordinateY, isMine);

				oracle.currentStatus();
			}

			else if (command.equals("c")) {
				oracle.currentStatus();
			}
			else if (command.equals("s")) {
				oracle.printScore();
			}
			else if (command.equals("r")) {
				oracle.restart();
			}
			else if (command.equals("rs")) {
				oracle.restartWithSameMap();
			}

			System.out.print("> ");
			command = input.next();
		}
		System.out.println("Commands Terminated.");
		input.close();
	}

	@SuppressWarnings("unused")
	public static Oracle autoMine(Oracle oracle, BoardMap map, ArrayList<Coordinate> ret) {		
		if(ret==null) { //잘못된 입력
			return null;
		}else if(ret.isEmpty()) { //게임 오버
			return oracle;
		}else {	//성공
			//map에 현재까지 coordinate를 기록
			for(Coordinate c : ret) {
				int x = c.getX();
				int y = c.getY();
				String value = Integer.toString(c.getValue());
				map.setCoordinate(x,y,value);
			}

			//oracle.currentStatus();
			System.out.println(map.toString());
			System.out.println("-----------------------------------");

			int nextX,nextY;

			//현재 map에서 확신되는 mine이 있는지 검사
			//확신된 mine이 있으면 mine 제거
			Point minePoint = map.findMine();
			if(minePoint.result) { //if find mine
				nextX = minePoint.x;
				nextY = minePoint.y;
				System.out.printf("remove mine (%d,%d)\n\n",nextX,nextY);
				ArrayList<Coordinate> removeList = oracle.actionPerform(nextX, nextY, 1);

				return autoMine(oracle,map,removeList);
			} else {
				Point nextPoint = map.findNext();

				if(nextPoint.result){
					//현재 map을 보고선 어느 좌표를 오픈해야할지 결정
					//결정된 좌표로 oracle 오픈
					nextX = nextPoint.x;
					nextY = nextPoint.y;
					System.out.printf("open coordinate (%d,%d)\n\n",nextX,nextY);
					ArrayList<Coordinate> openList = oracle.actionPerform(nextX, nextY, 0);

					return autoMine(oracle,map,openList);
				} else{
					Point patternPoint = map.findPattern();

					if(patternPoint.result) {
						nextX = patternPoint.x;
						nextY = patternPoint.y;
						System.out.printf("pattern found (%d,%d)\n\n",nextX,nextY);
						ArrayList<Coordinate> guess = oracle.actionPerform(nextX, nextY, 1);

						return autoMine(oracle,map,guess);
					}else {
						nextX = patternPoint.x;
						nextY = patternPoint.y;
						System.out.printf("random open (%d,%d)\n\n",nextX,nextY);
						ArrayList<Coordinate> guess = oracle.actionPerform(nextX, nextY, 0);
						
						return autoMine(oracle,map,guess);
					}
				}
			}
		}
	}
}

