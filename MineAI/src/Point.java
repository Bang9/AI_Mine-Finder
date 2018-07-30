
public class Point {
	public int x;
	public int y;
	public boolean result;

	Point(boolean result, int x, int y){
		if(result) {
			this.result=result;
			this.x=x;
			this.y=y;
		}
	}
	
	Point(boolean result){
		this.result=result;
	}

	Point(int x, int y){
		this.x=x;
		this.y=y;
	}

	public String toString() {
		String str = String.format("(%d,%d)",x,y);
		return str;
	}
}
