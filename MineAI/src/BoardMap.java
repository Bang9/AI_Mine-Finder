import java.util.ArrayList;
import java.util.Arrays;

import mineSweeper.Coordinate;

public class BoardMap {
	String[][] map;
	int mapSize;
	int tryCounter;
	int mineSize;

	BoardMap(String[][] map, int mapSize, int tryCounter, int mineSize){
		this.map=map;
		this.mapSize=mapSize;
		this.tryCounter=tryCounter;
		this.mineSize=mineSize;
	}

	BoardMap(int mapSize){
		this.map = new String[mapSize][mapSize];
		this.mapSize = mapSize;
		this.tryCounter = 0;

		if(mapSize==8)	mineSize=10;
		if(mapSize==16) mineSize=40;
		if(mapSize==32) mineSize=150;

		for(int i=0; i<mapSize; i++) {
			for(int j=0; j<mapSize; j++){
				map[i][j]="??";
			}
		}
	}

	public void setCoordinate(int x, int y, String value) {
		this.map[x][y] = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<mapSize; i++) {
			for(int j=0; j<mapSize; j++){
				sb.append(String.format("%3s", map[j][i]));
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public Point findMine() {
		//현재 맵에서 확정할수있는 지뢰의 좌표를 반환한다.
		for(int i=0; i<mapSize; i++) {
			for(int j=0; j<mapSize; j++){
				String[] neighbor = {"X","X","X","X","X","X","X","X"};
				if(map[j][i].equals("0") || map[j][i].equals("??") || map[j][i].equals("99")) continue;
				//현재 좌표 주변값들을 모두 검색
				// 0 1 2
				// 3 * 4
				// 5 6 7

				if(j-1>=0 && i-1>=0)			neighbor[0]=map[j-1][i-1];
				if(i-1>=0)						neighbor[1]=map[j][i-1];
				if(j+1<mapSize && i-1>=0)		neighbor[2]=map[j+1][i-1];

				if(j-1>=0)						neighbor[3]=map[j-1][i];
				if(j+1<mapSize)					neighbor[4]=map[j+1][i];

				if(j-1>=0 && i+1<mapSize)		neighbor[5]=map[j-1][i+1];
				if(i+1<mapSize)					neighbor[6]=map[j][i+1];
				if(j+1<mapSize && i+1<mapSize)	neighbor[7]=map[j+1][i+1];

				int nextX=999,nextY=999;
				int mineCount = 0;
				int closeTile = 0;
				int emptyTile = 0;

				for(int k=0 ;k<neighbor.length; k++){
					String item = neighbor[k];

					if(item.equals("99")) {
						mineCount++;
					}
					else if(item.equals("X")) {
						emptyTile++;
					}
					else if(item.equals("??")) {
						closeTile++;
						switch(k) {
						case 0: nextX=j-1; nextY=i-1; break;
						case 1: nextX=j; nextY=i-1; break;
						case 2: nextX=j+1; nextY=i-1; break;
						case 3: nextX=j-1; nextY=i; break;
						case 4: nextX=j+1; nextY=i; break;
						case 5: nextX=j-1; nextY=i+1; break;
						case 6: nextX=j; nextY=i+1; break;
						case 7: nextX=j+1; nextY=i+1; break;
						default: break;
						}
					}
				}

				if(closeTile+mineCount == Integer.parseInt(map[j][i])) {
					if(nextX==999 || nextY==999) continue;
					//String[] ret = {"true",Integer.toString(nextX),Integer.toString(nextY)};
					Point ret = new Point(true,nextX,nextY);
					System.out.printf("\ncurrent coordinate : (%d,%d)-%s\n",j,i,map[j][i]);
					this.tryCounter++;
					return ret;
				}
			}
		}
		return new Point(false);
	}

	public Point findNext() {
		//오픈되었고 주변에 지뢰가 밝혀진 타일들의 나머지 오픈되지 않은 타일들을 찾아서 반환한다.
		for(int i=0; i<mapSize; i++) {
			for(int j=0; j<mapSize; j++){
				String[] neighbor = {"X","X","X","X","X","X","X","X"};
				if(map[j][i].equals("0") || map[j][i].equals("??") || map[j][i].equals("99")) continue;

				//현재 좌표 주변값들을 모두 검색
				// 0 1 2
				// 3 * 4
				// 5 6 7
				if(j-1>=0 && i-1>=0)			neighbor[0]=map[j-1][i-1];
				if(i-1>=0)						neighbor[1]=map[j][i-1];
				if(j+1<mapSize && i-1>=0)		neighbor[2]=map[j+1][i-1];

				if(j-1>=0)						neighbor[3]=map[j-1][i];
				if(j+1<mapSize)					neighbor[4]=map[j+1][i];

				if(j-1>=0 && i+1<mapSize)		neighbor[5]=map[j-1][i+1];
				if(i+1<mapSize)					neighbor[6]=map[j][i+1];
				if(j+1<mapSize && i+1<mapSize)	neighbor[7]=map[j+1][i+1];

				int nextX=999,nextY=999;
				int mineCount = 0;
				int closeCount = 0;

				//				System.out.printf("%d,%d - item:%s\n",j,i,Arrays.toString(neighbor));
				//				if(j-1>=0 && i-1>=0) System.out.println(map[j-1][i-1]);

				for(int k=0 ;k<neighbor.length; k++){
					String item = neighbor[k];
					if(item.equals("99")) mineCount++;
					else if(item.equals("??")) {
						closeCount++;
						switch(k) {
						case 0: nextX=j-1;	nextY=i-1;	break;
						case 1: nextX=j;	nextY=i-1;	break;
						case 2: nextX=j+1;	nextY=i-1;	break;
						case 3: nextX=j-1;	nextY=i;	break;
						case 4: nextX=j+1;	nextY=i;	break;
						case 5: nextX=j-1;	nextY=i+1;	break;
						case 6: nextX=j;	nextY=i+1;	break;
						case 7: nextX=j+1;	nextY=i+1;	break;
						default: break;
						}
					}
				}
				if(closeCount==0) continue;

				if(mineCount == Integer.parseInt(map[j][i])) {
					if(nextX==999 || nextY==999 || nextX>mapSize || nextX<0 || nextY>mapSize || nextY<0) continue;
					//String[] ret = {"true",Integer.toString(nextX),Integer.toString(nextY)};
					Point ret = new Point(true,nextX,nextY);
					System.out.printf("\ncurrent coordinate:(%d,%d)-%s\n",j,i,map[j][i]);
					this.tryCounter++;
					return ret;
				}

			}
		}
		return new Point(false);
	}

	public Point findPattern() {
		//가중치를 계산하여 지뢰가 있을 확률이 낮은 타일을 반환한다.

		//일단 한번 쫙 돌아서, 맵 정보 확인
		//index -> coordinate value
		ArrayList<Point>[]	openTile  = new ArrayList[8];
		ArrayList<Point>	closeTile = new ArrayList<Point>();
		for(int i=0;i<8;i++) openTile[i]=new ArrayList<Point>();

		int allCount=0;
		int mineCount=0;

		for(int i=0;i<mapSize;i++) {
			for(int j=0;j<mapSize;j++) {
				if(!map[j][i].equals("??") && !map[j][i].equals("99")) {
					int coorVal = Integer.parseInt(map[j][i]);
					openTile[coorVal].add(new Point(j,i));
					allCount++;
				}else if(map[j][i].equals("??")) {
					closeTile.add(new Point(j,i));
				}else {
					mineCount++;
				}
			}
		}

		int remain = mineSize-mineCount;
		int close = closeTile.size();
		//System.out.printf("mineSize:%d , mineCount:%d , remainMine:%d , remainTile : %d\n",mineSize,mineCount,remain,close);

		int Probability = remain*100/close;
		//		if(Probability>17) {
		//pattern scan
		String[][] copy = new String[mapSize][mapSize];
		for(int i=0; i<mapSize; i++) {
			for(int j=0; j<mapSize; j++){
				copy[j][i] = map[j][i];
			}
		}

		BoardMap minify = simplifyMap(new BoardMap(copy,this.mapSize,this.tryCounter,this.mineSize));
		if(minify.scan()!=null)
			return minify.scan();
		else {
			//If can not found any pattern, return null(when returned null, program will generate random point)
			Point p =  closeTile.get((int)(Math.random()*closeTile.size()));
			this.tryCounter++;
			return new Point(p.x,p.y);
		}
		//		}else {
		//			Point p =  closeTile.get((int)(Math.random()*closeTile.size()));
		//			this.tryCounter++;
		//			return new Point(p.x,p.y);
		//		}
	}

	public Point scan() {
		// 0 1 2
		// 3 P 4
		// 5 6 7

		// 1-2-1 pattern
		for(int i=0; i<mapSize; i++) {
			for(int j=0; j<mapSize; j++){
				if(!map[j][i].equals("2")) continue;
				if(i-1>=0 && i+1<mapSize && j-1>=0 && j+1<mapSize) { //vertical
					if(map[j][i-1].equals("1") && map[j][i+1].equals("1")) {
						if(map[j-1][i-1].equals("0") && map[j-1][i].equals("0") && map[j-1][i+1].equals("0")) {//0,3,5
							if(map[j+1][i-1].equals("??"))  return new Point(true,j+1,i-1);
							if(map[j+1][i+1].equals("??"))  return new Point(true,j+1,i+1);
							if(map[j+1][i].equals("??"))	return new Point(false,j+1,i);
						}
						if(map[j+1][i-1].equals("0") && map[j+1][i+1].equals("0") && map[j+1][i].equals("0")) {//2,4,7
							if(map[j-1][i-1].equals("??"))  return new Point(true,j-1,i-1);
							if(map[j-1][i+1].equals("??"))  return new Point(true,j-1,i+1);
							if(map[j-1][i].equals("??"))	return new Point(false,j-1,i);
						}
					}
				}

				if(i-1>=0 && i+1<mapSize && j-1>=0 && j+1<mapSize) { //horizontal
					if(map[j-1][i].equals("1") && map[j+1][i].equals("1")) {
						if(map[j-1][i-1].equals("0") && map[j][i-1].equals("0") && map[j+1][i-1].equals("0")) {//0,1,2 == 0
							if(map[j-1][i+1].equals("??"))  return new Point(true,j-1,i+1);
							if(map[j+1][i+1].equals("??"))	return new Point(true,j+1,i+1);
							if(map[j][i+1].equals("??"))	return new Point(false,j,i+1);
						}
						if(map[j-1][i+1].equals("0") && map[j][i+1].equals("0") && map[j+1][i+1].equals("0")) {//0,1,2
							if(map[j-1][i-1].equals("??"))  return new Point(true,j-1,i-1);
							if(map[j+1][i-1].equals("??"))	return new Point(true,j+1,i-1);
							if(map[j][i-1].equals("??"))    return new Point(false,j,i-1);
						}
					}
				}
			}
		}

		//1-1-1 pattern
		for(int i=0; i<mapSize; i++) {
			for(int j=0; j<mapSize; j++){
				if(!map[j][i].equals("1")) continue;

				// map[j][i-1]
				// map[j][i]
				// map[j][i+1]
				if(i-1>=0 && i+1<mapSize && j-1>=0 && j+1<mapSize) { //vertical
					//1-1-1 linked vertical
					//양 사이드의 [j][i-2]와 [j][i+2]는 막혀서 없거나, 0이거나, ??여야 한다.
					if(map[j][i-1].equals("1") && map[j][i+1].equals("1")) {
						if( (i-2<0 && (map[j][i+2].equals("0") || map[j][i+2].equals("??"))) ||
								(i+2>=mapSize && (map[j][i-2].equals("0") || map[j][i-2].equals("??"))) ||
								((i-2>=0 && i+2<mapSize) &&
										(map[j][i+2].equals("0") || map[j][i+2].equals("??")) &&
										(map[j][i-2].equals("0") || map[j][i-2].equals("??"))
										)
								){
							if(map[j-1][i-1].equals("0") && map[j-1][i].equals("0") && map[j-1][i+1].equals("0")) {//0,3,5
								if(map[j+1][i].equals("??"))	return new Point(true,j+1,i);
								if(map[j+1][i-1].equals("??"))  return new Point(false,j+1,i-1);
								if(map[j+1][i+1].equals("??"))  return new Point(false,j+1,i+1);
							}
							if(map[j+1][i-1].equals("0") && map[j+1][i+1].equals("0") && map[j+1][i].equals("0")) {//2,4,7
								if(map[j-1][i].equals("??"))	return new Point(true,j-1,i);
								if(map[j-1][i-1].equals("??"))  return new Point(false,j-1,i-1);
								if(map[j-1][i+1].equals("??"))  return new Point(false,j-1,i+1);
							}
						}
					}
				}

				if(i-1>=0 && i+1<mapSize && j-1>=0 && j+1<mapSize) { //horizontal
					if(map[j-1][i].equals("1") && map[j+1][i].equals("1")) { 
							if( (j-2<0 && (map[j+2][i].equals("0") || map[j+2][i].equals("??"))) ||
								(j+2>=mapSize && (map[j-2][i].equals("0") || map[j-2][i].equals("??"))) ||
								((j-2>=0 && j+2<mapSize) &&
										(map[j+2][i].equals("0") || map[j+2][i].equals("??")) &&
										(map[j-2][i].equals("0") || map[j-2][i].equals("??"))
								)
							){
								if(map[j-1][i-1].equals("0") && map[j][i-1].equals("0") && map[j+1][i-1].equals("0")) {//init down
									if(map[j][i+1].equals("??"))	return new Point(true,j,i+1);
									if(map[j-1][i+1].equals("??"))  return new Point(false,j-1,i+1);
									if(map[j+1][i+1].equals("??"))	return new Point(false,j+1,i+1);
								}
								if(map[j-1][i+1].equals("0") && map[j][i+1].equals("0") && map[j+1][i+1].equals("0")) {//init up
									if(map[j][i-1].equals("??"))    return new Point(true,j,i-1);
									if(map[j-1][i-1].equals("??"))  return new Point(false,j-1,i-1);
									if(map[j+1][i-1].equals("??"))	return new Point(false,j+1,i-1);
								}
							}
					}
				}
			}
		}

		return null;
	}

	public static BoardMap simplifyMap(BoardMap in) {
		for(int i=0; i<in.mapSize; i++) {
			for(int j=0; j<in.mapSize; j++){
				String[] neighbor = {"X","X","X","X","X","X","X","X"};
				if(!in.map[j][i].equals("99")) continue;

				//map[j][i] = 99
				in.map[j][i] = "0";

				if(j-1>=0 && i-1>=0)				neighbor[0]=in.map[j-1][i-1];
				if(i-1>=0)							neighbor[1]=in.map[j][i-1];
				if(j+1<in.mapSize && i-1>=0)		neighbor[2]=in.map[j+1][i-1];

				if(j-1>=0)							neighbor[3]=in.map[j-1][i];
				if(j+1<in.mapSize)					neighbor[4]=in.map[j+1][i];

				if(j-1>=0 && i+1<in.mapSize)		neighbor[5]=in.map[j-1][i+1];
				if(i+1<in.mapSize)					neighbor[6]=in.map[j][i+1];
				if(j+1<in.mapSize && i+1<in.mapSize)neighbor[7]=in.map[j+1][i+1];

				int nextX=999,nextY=999;

				for(int k=0 ;k<neighbor.length; k++){
					String item = neighbor[k];
					if(!item.equals("99") && !item.equals("X") && !item.equals("0") && !item.equals("??")) {
						switch(k) {
						case 0: nextX=j-1; nextY=i-1; break;
						case 1: nextX=j; nextY=i-1; break;
						case 2: nextX=j+1; nextY=i-1; break;
						case 3: nextX=j-1; nextY=i; break;
						case 4: nextX=j+1; nextY=i; break;
						case 5: nextX=j-1; nextY=i+1; break;
						case 6: nextX=j; nextY=i+1; break;
						case 7: nextX=j+1; nextY=i+1; break;
						default: break;
						}
						in.map[nextX][nextY] = Integer.toString(Integer.parseInt(in.map[nextX][nextY])-1);
					}
				}
			}
		}
		//System.out.println(in.toString());
		return in;
	}

	public void getResult() {
		int closeTile=0;
		int openTile=0;
		int mineTile=0;
		for(int i=0;i<mapSize;i++) {
			for(int j=0;j<mapSize;j++) {

				if(map[j][i].equals("??")) {
					closeTile++;
				}
				else if(map[j][i].equals("99")) {
					mineTile++;
				}
				else {
					openTile++;
				}
			}
		}

		System.out.printf("\n====RESULT====\ntry count : %d\n",tryCounter);
	}

	public int getCloseTile() {
		int closeTile=0;
		for(int i=0;i<mapSize;i++) {
			for(int j=0;j<mapSize;j++) {
				if(map[j][i].equals("??")) {
					closeTile++;
				}
			}
		}

		return closeTile;
	}

	public int getRemainMine() {
		int mine=0;
		for(int i=0;i<mapSize;i++) {
			for(int j=0;j<mapSize;j++) {
				if(map[j][i].equals("99")) {
					mine++;
				}
			}
		}

		return mineSize-mine;
	}

}


