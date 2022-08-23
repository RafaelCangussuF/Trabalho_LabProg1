package org.example;


public class Aeroporto {
    private String sigla;
    private String Latitude, longitude;
    private String Estado, Municipio;

    public Aeroporto(String sigla1, String Estado1, String Municipio1, String Latitude1, String longitude1){
        setEstado(Estado1);
        setLatitude(Latitude1);
        setSigla(sigla1);
        setMunicipio(Municipio1);
        setLongitude(longitude1);
    }

    public Aeroporto() {

    }

    public String getSigla(){
        return this.sigla;
    }

    public String getEstado(){
        return this.Estado;
    }
    public String getMunicipio(){
        return this.Municipio;
    }
    public String getLatitude(){
        return this.Latitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
    public void setSigla(String sigla1){
        this.sigla = sigla1;
    }

    public void setLatitude(String l){
        this.Latitude = l;
    }
    public void setLongitude(String l){
        this.longitude = l;
    }
    public void setEstado(String state){
        this.Estado = state;
    }
    public void setMunicipio(String c){
        this.Municipio = c;
    }

    public static void main(String[] args) {
        Aeroporto aero = new Aeroporto();
        aero.setSigla("SdSDDS");
        System.out.println(aero.getSigla());
    }

}
