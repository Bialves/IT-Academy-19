package banco.financeiro;

public class Transacao {
    private String data;
    private TipoTransacao tipo;
    private String categoria;
    private String descricao;
    private double valor;
    private double saldoTransacao;


    public Transacao(String d, TipoTransacao t, String c, String desc, double v) {
        this.data = d;
        this.tipo = t;
        this.categoria = c;
        this.descricao = desc;
        this.valor = v;
    }

    public String getData() {
        return data;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public double getSaldoTransacao() {return saldoTransacao;}

    public void setSaldoTransacao(double saldoTransacao) {this.saldoTransacao = saldoTransacao;}
}
