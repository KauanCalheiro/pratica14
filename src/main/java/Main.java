import com.univates.models.Model;
import com.univates.screens.TelaCadastro;

public class Main 
{
    /*
     * TODO:
     *  DOING - Clicar duas vezes em um item da tabela abre uma janela para editar ou excluir
     *  DOING - Fazer a tela de edicao
     *  Alterar tamanho da celula da tabela 
     *  Funcao de exportar para planilha
     *  Funcao de importar de planilha    - Talvez nao de tempo
     *  Funcao de baixar planilha modelo  - Talvez nao de tempo
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
