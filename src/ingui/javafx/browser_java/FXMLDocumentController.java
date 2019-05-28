package ingui.javafx.browser_java;

import ingui.html.browser_java.IndexControlador;
import innui.archivos.Archivos;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author daw
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private WebView webView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boolean ret = true;
        String [] error = { "" };
        WebEngine webEngine = webView.getEngine();
        ret = poner_escuchador_de_url(error);
        String archivo = "/ingui/html/browser_java/recursos/index.html";
        String texto = null;
        String ruta = Archivos.leer_ruta_base(this.getClass(), error);
        if (ruta != null) {
            texto = Archivos.leer_archivo_texto(archivo, error);
        }
        if (texto != null) {
            texto = texto.replaceAll("\\$\\{\\s*browser_java_ruta\\s*\\}", ruta);
            webEngine.loadContent(texto);    
        }
    }    
    
    public boolean poner_escuchador_de_url(String [] error)
    {
        boolean ret = true;
        WebEngine webEngine = this.webView.getEngine();
        ReadOnlyStringProperty readOnlyStringProperty = webEngine.locationProperty();
        readOnlyStringProperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                boolean ret = true;
                String contenido;
                String [] error = { "" };
                String url = newValue;
                try {
                    if (url.startsWith("http://browser_java/index")) {
                        contenido = IndexControlador.procesar(url, error);
                        if (contenido != null) {
                            ret = cargar_contenido(contenido, "text/html", error);
                        } else {
                            ret = false;
                        }
                    }
                } catch (Exception e) {
                    error [0] = e.getMessage();
                    if (error[0] == null) {
                        error[0] = "";
                    }
                    error[0] = "Error al analizar el cambio de URL. ";
                    ret = false;
                }
                if (ret == false) {
                    poner_error(error[0]);
                }
            }           
        });
        return ret;
    }

    public boolean poner_error(String mensaje) {
        String [] error = { "" };
        return cargar_contenido(mensaje, "text/html", error);
    }
    
    public boolean cargar_contenido(String contenido, String tipo_contenido, String [] error) {
        boolean ret = true;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               WebEngine webEngine =  webView.getEngine();
               webEngine.loadContent(contenido, tipo_contenido);
            }
            
        });
        return ret;
    }
    
}
