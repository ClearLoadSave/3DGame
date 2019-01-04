package core;

public interface IGameLogic {
	
	void input(Display display);
	
	void update(float interval);
	
	void render(Display display);
	
	void cleanup();

	void init(Display display) throws Exception;
	
}
