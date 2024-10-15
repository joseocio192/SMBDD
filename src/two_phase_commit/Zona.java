package two_phase_commit;

public enum Zona {
    NORTE(new String[] {
            "Baja California", "Baja California Sur", "Sonora", "Chihuahua", "Coahuila",
            "Nuevo León", "Tamaulipas", "Durango", "Sinaloa"
    }),
    CENTRO(new String[] {
            "Aguascalientes", "Zacatecas", "San Luis Potosí", "Guanajuato", "Querétaro",
            "Jalisco", "Michoacán", "Estado de México", "Hidalgo", "Morelos",
            "Tlaxcala", "Ciudad de México", "Colima", "Nayarit"
    }),
    SUR(new String[] {
            "Chiapas", "Guerrero", "Oaxaca", "Puebla", "Tabasco", "Veracruz",
            "Campeche", "Yucatán", "Quintana Roo"
    });

    private final String[] estados;

    Zona(String[] estados) {
        this.estados = estados;
    }

    public String[] getEstados() {
        return estados;
    }

    public boolean contieneEstado(String nombreEstado) {
        for (String estado : estados) {
            if (estado.equalsIgnoreCase(nombreEstado)) {
                return true;
            }
        }
        return false;
    }
}
