package py.com.nikosoft.depositos.entities;

public class Estante {

    private int idEstante;
    private String codigo;
    private Integer idDeposito; // puede ser null si no está asignado a un depósito

    public Estante() {
    }

    public Estante(int idEstante, String codigo, Integer idDeposito) {
        this.idEstante = idEstante;
        this.codigo = codigo;
        this.idDeposito = idDeposito;
    }

    public int getIdEstante() {
        return idEstante;
    }

    public void setIdEstante(int idEstante) {
        this.idEstante = idEstante;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(Integer idDeposito) {
        this.idDeposito = idDeposito;
    }
}
