package banco;

import java.util.LinkedList;

public class InstituicoesBancarias {
    private LinkedList<Conta> contas;

    public InstituicoesBancarias() {
        contas = new LinkedList<>();
    }

    public LinkedList<Conta> getContas() {
        return contas;
    }

    public Conta getConta(int numConta) {
        for (Conta c : contas) {
            if (c.getNumConta() == numConta) {
                return c;
            }
        }
        return null;
    }

    public boolean cadastrarConta(Conta conta) {
        if (!consultarConta(conta.getNumConta())) {
            return contas.add(conta);
        }
        return false;
    }

    public boolean consultarConta(int numConta) {
        for (Conta c : contas) {
            if (c.getNumConta() == numConta) {
                return true;
            }
        }
        return false;
    }

    public boolean removerConta(int numConta) {
        for (Conta c : contas) {
            if (c.getNumConta() == numConta) {
                return contas.remove(c);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Conta  |  Titular  |  Banco  |  Agência  |  Saldo\n");

        for (Conta c : contas) {
            s.append(
                    "> "
                    + c.getNumConta() + " | "
                    + c.getTitular() + " | "
                    + c.getBanco() + " | "
                    + c.getAgencia() + " | "
                    + c.getSaldo() + "\n"
            );
        }
        return s.toString();
    }
}
