package banco;

import banco.financeiro.Transacao;

import java.util.LinkedList;

public class Conta {
    private LinkedList<Transacao> transacoes;
    private int numConta;
    private String titular;
    private String banco;
    private int agencia;
    private double saldo;

    public Conta(int numConta, String titular, String banco, int agencia) {
        this.numConta = numConta;
        this.titular = titular;
        this.banco = banco;
        this.agencia = agencia;
        this.saldo = 0;

        transacoes = new LinkedList<Transacao>();
    }

    public int getNumConta() {
        return numConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getTitular() {
        return titular;
    }

    public String getBanco() {
        return banco;
    }

    public int getAgencia() {
        return agencia;
    }

    public LinkedList<Transacao> getTransacoes() {
        return transacoes;
    }

    public boolean adicionarTransacao(Transacao t) {
        if (!transacoes.contains(t)) {
            return transacoes.add(t);
        }
        return false;
    }

    public Transacao removerTransacao(Transacao t) {
        for (Transacao trans : transacoes) {
            if (trans.equals(t)) {
                return trans;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Data   |   Tipo   |   Categoria   |    Descrição    |   Valor  |  Saldo\n");

        for (Transacao t : transacoes) {
            s.append(
                    t.getData() + " | "
                    + t.getTipo() + " | "
                    + t.getCategoria() + " | "
                    + t.getDescricao() + " | "
                    + "R$ " + t.getValor() + " | "
                    + "R$ " + t.getSaldoTransacao() + "\n"
            );
        }
        return s.toString();
    }
}
