package com.example.nelson.mp3fly;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nelson on 04/04/2016.
 */
public class Tocamedia extends MediaPlayer {
    private Context context;
    private File cartao = Environment.getExternalStorageDirectory();
    private List<String> listademusicas;
    private List<String> minhaLista;
    private FileFilter fileFilter;
    private FileFilter novoFilter;
    private FileFilter nomedir;
    private String camimho;
    private Integer duracao;

    private List<String> soamusica;
    private List<String> musicas;
    private List<String> diretorio;
    private String[] arqs;
    private StorageManager storageManager;
    private Environment environment;
    private String[] nomediretorio;

    public Tocamedia() {
    }

    public void tocar() {
        if (isPlaying()) {
            stop(); //Para a musica antes
        }
        start();
    }

    public void pausa() {
        pause();
    }

    public void proxima(Integer triha) {
        //  selectTrack(triha);
    }


    public void parar() {
        stop();
    }

    public Integer tempodamusica() {

        if (isPlaying()) {

            duracao = getDuration();
        } else {
            duracao = 0;
        }

        return duracao;
    }

    public Integer posicao() {
        return getCurrentPosition();
    }

    public void abrir(Integer musica) {

        try {
            if (isPlaying()) {
                stop(); //Para a musica antes se estiver tocando
            }
            // musicas.contains(musica);
            reset();
            String teste = musicas.get(musica).toString();
            setDataSource(teste);
            prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /*
     Verifica se esta tocando
      */
    public boolean naotocando() {
        return isPlaying();
    }


    //Lista os arquvios
    public List<String> listarq() throws IOException {
        soamusica = new ArrayList<String>();
        musicas = new ArrayList<String>();

        File internaldisk0= new File("/storage");
        File internaldisk1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        File cartao1[]= new File("/storage/").listFiles();

        for(File file:cartao1) {
            if (file == cartao1[0]) {


                File musica[] = new File(String.valueOf(file.getAbsolutePath() + "/MUSICAS/")).listFiles();
                File musicaintera[] = new File(String.valueOf(file.getAbsolutePath() + "/Music/")).listFiles();


                if(musica!=null){

                for (File file1 : musica) {

                    musicas.add(file1.getAbsolutePath());
                    soamusica.add(file1.getName());
                 }

                }

                if(musicaintera!=null){

                    for (File file1Externo : musicaintera) {
                        musicas.add(file1Externo.getAbsolutePath());
                        soamusica.add(file1Externo.getName());
                    }
                }

            }

            else if (file == cartao1[1]) {
                File sd1[] = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath()).listFiles();
                if(sd1!=null) {
                    for (File file2 : sd1) {

                        musicas.add(file2.getAbsolutePath());
                        soamusica.add(file2.getName());


                    }
                }
            }
            else if (file == cartao1[2]) {
                File sd2[] = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath()).listFiles();
                if(sd2!=null) {
                    for (File file3 : sd2) {//if (file1.getAbsolutePath().equals("/MUSICA/"))
                        musicas.add(file3.getAbsolutePath());
                        soamusica.add(file3.getName());


                    }
                }
            }


        }




        /*
        * Pega as pastas do crtao interno e externo
         */

       final File storage= new File(internaldisk1.getAbsolutePath());
        //final File storage= new File(Audio.Media.IS_MUSIC);


        //Se for um diretorio d emusica adciona na lista


        /**
         * Faz o filtro
         */
        FilenameFilter fileNameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if(name.lastIndexOf('.')>0)
                {
                    // get last index for '.' char
                    int lastIndex = name.lastIndexOf('.');

                    // get extension
                    String str = name.substring(lastIndex);

                    // match path name extension
                    if(str.equals(".mp3"))
                    {
                        return true;
                    }
                }
                return false;
            }
        };




        /**
        if (storage.isDirectory()) {


            File[] list = storage.listFiles(fileNameFilter);

            if(list.length > 0 ){
                List<File> names = Arrays.asList(list);
                Collections.sort(names);
                for (File f2 :list) {
                    musicas.add(f2.getAbsolutePath());
                    soamusica.add(f2.getName());
                    soamusica.indexOf(f2);
                    // Log.i("PASTA.:/extSdCard/Music", f.getName());
                }

            }

        }
     **/









        //Le o cartão externo  do celular na pasta Music
        /**
         * Se o diretorio nao e vazio entao alimenta a lista
         */
        /**
        if (!diretorio.isEmpty()) {


            }
         **/

        Collections.indexOfSubList(musicas, soamusica);


        return soamusica;
    }

    public String getCamimho() {
        return camimho;
    }

    public void setCamimho(String camimho) {
        this.camimho = camimho;
    }


}
