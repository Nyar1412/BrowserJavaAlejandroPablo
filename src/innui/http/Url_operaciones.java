/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.http;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

/**
 *
 * @author daw
 */
public class Url_operaciones {

    public static boolean extraer_parametros_query(URL url
            , Map<String, String> query_mapa
            , String [] error)
    {
        boolean ret = true;
        try {
            String query = url.getQuery();
            // ?nombre=valor&nombre1=&nombre2=valor+2
            String [] query_array = query.split("&");
            String [] clave_valor_array;
            String clave;
            String valor;
            for (String trozo: query_array) {
                clave_valor_array = trozo.split("=");
                if (clave_valor_array.length == 2) {
                    clave = clave_valor_array[0];
                    valor = clave_valor_array[1];
                } else if (clave_valor_array.length == 1) {
                    clave = clave_valor_array[0];
                    valor = "";
                } else {
                    error[0] = "Query mal construida. ";
                    ret = false;
                    break;
                }
                clave = URLDecoder.decode(clave, "UTF-8");
                valor = URLDecoder.decode(valor, "UTF-8");
                query_mapa.put(clave, valor);
            }  
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = "";
            }
            error[0] = "Error en extraer_parametros_query. " + error[0];
            ret = false;
        }
        return ret;
    }
    
}
