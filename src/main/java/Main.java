import com.univates.models.Dao;
import com.univates.screens.TelaCadastro;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        Dao.setShowSql(true);
        Dao.setDb(Dao.DB_SQLITE);
        
        TelaCadastro telaCadastro = new TelaCadastro();
        telaCadastro.setVisible(true);  
        
        Dao.closeConnection();
    }
}
