import com.univates.models.Model;
import com.univates.screens.TelaCadastro;

public class Main 
{
    /*
     * TODO:
     *  1 - Trocar o LocalDateTime por Timestamp
     *  2 - Funcao para poder ir para a tela de cadastro pela tela de login (feito)
     *  3 - Clicar duas vezes em um item da tabela abre uma janela para editar ou excluir
     *  4 - Fazer a tela de edicao
     *  DONE 5 - Implementar o JDBC 
     *  6 - Funcao de exportar para planilha
     *  7 - Funcao de importar de planilha    - Talvez nao de tempo
     *  8 - Funcao de baixar planilha modelo  - Talvez nao de tempo
     */
    public static void main(String[] args) throws Exception 
    {
        Model.setShowSql(true);
        Model.setDb(Model.DB_SQLITE);
        
        TelaCadastro telaCadastro = new TelaCadastro();
        telaCadastro.setVisible(true);  
        
        Model.fecharConexao();
    }
}
