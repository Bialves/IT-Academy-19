package banco.financeiro;

public enum TipoTransacao {
    RECEITA("Receita"),
    DESPESA("Despesa"),
    SAQUE("Saque"),
    DEPOSITO("Depósito");

    private String nome;

    TipoTransacao(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
