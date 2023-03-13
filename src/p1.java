
public class p1 {
	public static void main(String[] argv) {
		if (argv.length < 1) {
			BankingSystem.init("db.properties");
			BankingSystem.testConnection();
			BatchInputProcessor.run("db.properties");
		} else {
			BankingSystem.init(argv[0]);
			BankingSystem.testConnection();
//			BatchInputProcessor.run(argv[0]);
			BankingSystem.runInterface();
		}
	}
}
