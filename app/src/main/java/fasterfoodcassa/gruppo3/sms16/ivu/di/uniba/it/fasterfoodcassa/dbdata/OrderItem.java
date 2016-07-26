package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata;

public class OrderItem {
    private String nome;
    private String prezzo;
    private String quantita;
    private Long position;

    public OrderItem() {
    }

    public String getNome() {
        return nome;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public String getQuantita() {
        return quantita;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }
}

