import com.moy.auto.core.AutoGenerator;
import com.moy.auto.util.OutputUtil;


public class Test {

	public static void main(String[] args) {
		AutoGenerator ag=AutoGenerator.getAutoGenerator();
		String[] a= {"article"};
//		ag.generateDto();
//		ag.generateEntity();
	//	ag.generateDao();
//		ag.generateService();
//		ag.generateController();
		ag.generateAll();
	}
}
