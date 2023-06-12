import banco.*;
import banco.financeiro.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SistemaGerenciamento {

    class OrderDate implements Comparator<Transacao> {
        // Inner class para reordenação da lista de transações
        @Override
        public int compare(Transacao one, Transacao second) {
            return one.getData().compareTo(second.getData());
        }
    }

    private Scanner scan;
    private InstituicoesBancarias instituicoes;

    public SistemaGerenciamento() {
        scan = new Scanner(System.in);
        instituicoes = new InstituicoesBancarias();

        instituicoes.cadastrarConta(new Conta(0,"João","Bradesco",1111));
        instituicoes.cadastrarConta(new Conta(1,"Maria","Banco do Brasil",2222));
        instituicoes.cadastrarConta(new Conta(2,"Lucia","Bradesco",3333));
    }
    // Método principal
    public void executa() {
        boolean run = true;

        do {
            menu();
            int opcao = scan.nextInt();
            System.out.println();

            switch (opcao) {
                case 1:
                    cadastro(); // OK
                    break;
                case 2:
                    remover(); // OK
                    break;
                case 3:
                    mesclrar(); // OK
                    break;
                case 4:
                    try {
                        incluirTransacao(); // OK
                    } catch (InputMismatchException e) { // AVALIAR A EXCEÇÃO
                        System.err.format("Entrada de dados inválida: %s%n", e);
                        break;
                    } catch (DateTimeException e) {
                        System.err.format("Data inválida: %s%n", e.getCause());
                    } catch (IllegalArgumentException e) {
                        System.err.format("Tipo de transação inválido: %s%n", e);
                    }
                    break;
                case 5:
                    extrato(); // OK
                    break;
                case 6:
                    resumoContas(); // OK
                    break;
                case 7:
                    try {
                        resumoMes(); // OK
                    } catch (DateTimeException e) {
                        System.err.format("Data inválida: %s%n", e.getCause());
                    }
                    break;
                case 8:
                    System.out.println("... Saindo");
                    run = false;
                    break;
                default:
                    System.out.println("ERRO: Opção inválida! Tente novamente");
            }
        } while(run);
    }


    public void menu() {
        System.out.println("========= MENU =========");
        System.out.println("1. Cadastrar conta");
        System.out.println("2. Remover conta");
        System.out.println("3. Mesclar contas");
        System.out.println("4. Incluir transação");
        System.out.println("5. Imprimir extrato");
        System.out.println("6. Resumo geral");
        System.out.println("7. Resumo mês");
        System.out.println("8. Encerrar");
        System.out.print("> ");
    }

    public void cadastro() {
        scan.nextLine();
        System.out.println("===== Cadastrar conta =====");
        System.out.println("Informe o nome do Banco");
        System.out.print("> ");
        String nomeBanco = scan.nextLine();
        System.out.println("Informe a agência");
        System.out.print("> ");
        int numAgencia = scan.nextInt();
        System.out.println("Informe o número da conta");
        System.out.print("> ");
        int numConta = scan.nextInt();
        scan.nextLine();
        System.out.println("Informe o nome do titular da conta");
        System.out.print("> ");
        String titular = scan.nextLine();


        if (instituicoes.cadastrarConta(new Conta(numConta, titular, nomeBanco, numAgencia))) {
            System.out.println(instituicoes.toString());
            System.out.println("... Conta cadastrada com sucesso!");
        } else {
            System.out.println("ERRO: Conta já cadastrada anteriormente!");
        }
    }

    public void remover() {
        System.out.println("======= Exclusão de conta ======");
        System.out.println("Informe o número da conta");
        System.out.print("> ");
        int numConta = scan.nextInt();

        if (instituicoes.consultarConta(numConta)) {
            System.out.println("Conta encontrada! \n " +
                    "Ao confirmar a exclusão da conta, todos os dados vinculados a mesma, serão permanentemente perdidos."
            );

            System.out.println("1. Excluir conta");
            System.out.println("2. Cancelar operação");
            System.out.print("> ");
            int op = scan.nextInt();

            if (op == 1) {
                if (instituicoes.removerConta(numConta)) {
                    System.out.println(instituicoes.toString());
                    System.out.println("Conta excluída com sucesso!");
                } else {
                    System.out.println("Não foi possível concluir a operação...");
                }
            } else if (op == 2) {
                System.out.println(
                        "Operação cancelada! Conta de número: "
                        + numConta
                        + " mantida."
                );
            } else {
                System.out.println("ERRO: operação inválida!");
            }
        } else {
            System.out.println("ERRO: conta não encontrada!");
        }
    }

    public void mesclrar() {
        System.out.println("======= Mesclrar contas =======");
        System.out.println("Informe o número da conta destinatária das transações (conta que será mantida após a mesclagem)");
        System.out.print("> ");
        int contaDest = scan.nextInt();
        System.out.println("\nInforme o número da conta que remeterá suas transações (conta que excluída após a mesclagem)");
        System.out.print("> ");
        int contaRem = scan.nextInt();

        if (instituicoes.consultarConta(contaDest) && instituicoes.consultarConta(contaRem)) {
            Conta destinario = instituicoes.getConta(contaDest);
            Conta remetente = instituicoes.getConta(contaRem);

            for (Transacao t : remetente.getTransacoes()) {
                destinario.adicionarTransacao(remetente.removerTransacao(t));
            }
            //Reordena a lista de transações
            Collections.sort(destinario.getTransacoes(), new OrderDate());
            if (instituicoes.removerConta(contaRem)) {
                System.out.println("Contas mescladas com sucesso!");
                System.out.println("Contas de número: " + contaRem + " excluída...");
                System.out.println("Lista de Transações da conta destinatária:");
                System.out.println(destinario.toString());
            }
        } else {
            System.out.println("ERRO: não foi possível encontrar as contas informadas!");
        }
    }

    public void incluirTransacao() throws InputMismatchException {
        System.out.println("======= Incluir transação ======");
        System.out.println("Informe o número da conta");
        System.out.print("> ");
        int numConta = scan.nextInt();

        if (instituicoes.consultarConta(numConta)) {
            Conta conta = instituicoes.getConta(numConta);
            do {
                System.out.println("Saldo atual da conta: " + conta.getSaldo());

                System.out.println("Deseja mesmo prosseguir?");
                System.out.println("0. Sim");
                System.out.println("1. Retornar ao menu");
                System.out.print("> ");
                int op = scan.nextInt();

                if (op == 0) {
                    System.out.println("\nInforme o dia da transação");
                    System.out.print("> ");
                    int dia = scan.nextInt();
                    System.out.println("Informe o mês da transação");
                    System.out.print("> ");
                    int mes = scan.nextInt();
                    System.out.println("Informe o ano da transação");
                    System.out.print("> ");
                    int ano = scan.nextInt();
                    System.out.println();
                    LocalDate dateValid = LocalDate.of(ano, mes, dia); // Apenas para validar a data
                    String data = String.valueOf(dateValid);

                    scan.nextLine();
                    System.out.println("Informe o tipo da transação");
                    System.out.print("> ");
                    TipoTransacao tipo = TipoTransacao.valueOf(scan.nextLine().toUpperCase());

                    if (tipo != null && data != null) {
                        System.out.println("Informe a categoria da transação");
                        System.out.print("> ");
                        String categoria = scan.nextLine();
                        System.out.println("Informe a descrição da transação");
                        System.out.print("> ");
                        String descricao = scan.nextLine();
                        System.out.println("Informe o valor da transação");
                        System.out.print("> ");
                        double valor = scan.nextDouble();

                        if (tipo == TipoTransacao.RECEITA) { // Recebendo
                            if (valor < 0) {
                                System.out.println("ERRO: valor da transação inválido!");
                            } else {
                                Transacao trans = new Transacao(data, tipo, categoria, descricao, valor);
                                if (conta.adicionarTransacao(trans)) {
                                    trans.setSaldoTransacao(conta.getSaldo() + valor); // Informa o saldo atual da transação
                                    Collections.sort(conta.getTransacoes(), new OrderDate()); // Reordena as transações

                                    conta.setSaldo(conta.getSaldo() + valor); // Atualiza saldo da conta

                                    System.out.println("Transação incluida com sucesso!");
                                } else {
                                    System.out.println("ERRO: não foi possível incluir uma nova transação!");
                                }
                            }
                        } else if (tipo == TipoTransacao.DESPESA) { // Gastando
                            if (valor > conta.getSaldo() || valor < 0) {
                                System.out.println("ERRO: valor da transação inválido!");
                            } else {
                                Transacao trans = new Transacao(data, tipo, categoria, descricao, valor);
                                if (conta.adicionarTransacao(trans)) {
                                    trans.setSaldoTransacao(conta.getSaldo() - valor); // Informa o saldo atual da transação
                                    Collections.sort(conta.getTransacoes(), new OrderDate()); // Reordena as transações

                                    conta.setSaldo(conta.getSaldo() - valor); // Atualiza saldo da conta

                                    System.out.println("Transação incluida com sucesso!");
                                } else {
                                    System.out.println("ERRO: não foi possível incluir uma nova transação!");
                                }
                            }
                        }
                    } else {
                        System.out.println("ERRO: campos inválidos, impossível concluir a operação!");
                    }
                } else if (op == 1) {
                    break;
                } else {
                    System.out.println("ERRO: opção inválida! Tente novamente...");
                }
            } while (true);
        } else {
            System.out.println("ERRO: conta informada não encontrada!");
        }
    }


    public void extrato() {
        System.out.println("======= Imprimir extrato ======");
        System.out.println("Informe o número da conta");
        System.out.print("> ");
        int numConta = scan.nextInt();

        if (instituicoes.consultarConta(numConta)) {
            Conta conta = instituicoes.getConta(numConta);
            /*
             *    for (Transacao t : conta.getTransacoes()) { // Ajusta os saldos das transações
             *       if (t.getTipo() == TipoTransacao.RECEITA) {
             *           t.setSaldoTransacao(conta.getSaldo() + t.getValor());
             *       } else if (t.getTipo() == TipoTransacao.DESPESA) {
             *           if (t.getValor() <= conta.getSaldo()) {
             *               t.setSaldoTransacao(conta.getSaldo() - t.getValor());
             *           }
             *       }
             *   }
             */

            System.out.println("----------------------------------EXTRATO-----------------------------------");
            System.out.print(conta.toString());
            System.out.println("----------------------------------------------------------------------------");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            System.out.println("> Extrato impresso em: " + "yyyy/MM/dd HH:mm:ss-> "+dtf.format(LocalDateTime.now()) + "\n");

        } else {
            System.out.print("ERRO: conta nõa encontrada!");
        }
    }

    public void resumoContas() {
        double saldoUser = 0;

        for (Conta c : instituicoes.getContas()) { // Calcula a somatória de saldos
            saldoUser += c.getSaldo();
        }

        System.out.println("-----------------------------RESUMO CONTAS------------------------------");
        System.out.println(instituicoes.toString());
        System.out.println("\nSaldo total do usuário: " + saldoUser);
        System.out.println("------------------------------------------------------------------------");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println("> Resumo impresso em: " + "yyyy/MM/dd HH:mm:ss-> "+dtf.format(LocalDateTime.now()) + "\n");
    }

    public void resumoMes() throws DateTimeException {
        System.out.println("======= Resumo por Mês ======");
        System.out.println("Informe o ano: modelo 'yyyy'");
        System.out.print("> ");
        Year ano = Year.of(scan.nextInt());
        System.out.println("Informe o dígito do mês");
        System.out.print("> ");
        Month mes = Month.of(scan.nextInt()); // Validar o mês

        ArrayList<Transacao> transacaosMes = new ArrayList<>();
        for (Conta c : instituicoes.getContas()) {
            for (Transacao t : c.getTransacoes()) {
                LocalDate data = LocalDate.parse(t.getData());
                if (data.getYear() == ano.getValue()) {
                    if (data.getMonthValue() == mes.getValue()) {
                        transacaosMes.add(t); // Se a transação pertencer ao mês e ano informados
                    }
                }
            }
        }

        int contReceita = 0;
        double somaReceita = 0;
        int contDespesa = 0;
        double somaDespesa = 0;
        for (Transacao t : transacaosMes) { // Descobre a quantidade de cada tipo
            if (t.getTipo() == TipoTransacao.RECEITA) {
                somaReceita += t.getValor();
                contReceita++;
            } else if (t.getTipo() == TipoTransacao.DESPESA) {
                somaDespesa += t.getValor();
                contDespesa++;
            }
        }

        System.out.println("------------RESUMO MÊS------------");
        System.out.println("Transações de RECEITA: " + contReceita);
        System.out.println("Gastos: " + somaReceita + "\n");
        System.out.println("Transações de DESPESA: " + contDespesa);
        System.out.println("Gastos: " + somaDespesa);
        System.out.println("----------------------------------");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println("> Resumo impresso em: " + "yyyy/MM/dd HH:mm:ss-> "+dtf.format(LocalDateTime.now()) + "\n");
    }
}
