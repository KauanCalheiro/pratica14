import com.univates.models.Dao;
import com.univates.screens.TelaCadastro;

public class Main 
{
    /*
     * TODO:
     *  Alterar tamanho da celula da tabela 
     *  Funcao de exportar para planilha
     *  Funcao de importar de planilha    - Talvez nao de tempo
     *  Funcao de baixar planilha modelo  - Talvez nao de tempo
     */
    public static void main(String[] args) throws Exception 
    {
        Dao.setShowSql(true);
        Dao.setDb(Dao.DB_SQLITE);
        
        TelaCadastro telaCadastro = new TelaCadastro();
        telaCadastro.setVisible(true);  
        
        Dao.closeConnection();
    }
}
