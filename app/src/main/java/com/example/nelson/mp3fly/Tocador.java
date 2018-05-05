/**
 * Projeto de MP3
 * Autor: Nelson Seixas
 */
package com.example.nelson.mp3fly;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class Tocador extends AppCompatActivity {
    private ImageButton imageBtproximo; //Botão de sair
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageBtSair;
    private ImageButton imageBtAnterior;
    private Integer posicaonalista;
    private Button btsdcad;
    private Button btsdExterno;
    private TabHost tabHost;
    private Button botaoabrir1;
    private TabHost.TabSpec tabSpec;
    private ListView listaarq;
    private View view;
    private TextView textView;
    private TextView nomenatela;
    private List<String> musicas;
    private String caminhocompleto;
    private String nomedamusica;
    private Tocamedia tocamedia;
    private ProgressBar progressBar;
    private Handler mHandler = new Handler();
    private Boolean faz;
    private Boolean sairfora;
    private SeekBar barra;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tocamedia = new Tocamedia();
        setContentView(R.layout.activity_tocador);

        tabHost = (TabHost) findViewById(R.id.tabHost1);
        tabHost.setup();
        tabHost.setCurrentTab(0);
        tabSpec = tabHost.newTabSpec("Aba1");
        tabSpec.setContent(R.id.lista);
        tabSpec.setIndicator("Musicas");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Aba2");
        tabSpec.setContent(R.id.musica);
        tabSpec.setIndicator("Play");
        tabHost.addTab(tabSpec);

        imageBtSair = (ImageButton) findViewById(R.id.imageBtSair);
        listaarq = (ListView) findViewById(R.id.listademusicas);
        textView = (TextView) findViewById(R.id.nomedamusica);
        imageButton2 = (ImageButton) findViewById(R.id.player);
        imageButton3 = (ImageButton) findViewById(R.id.pausa);
        imageBtproximo = (ImageButton) findViewById(R.id.proximo);
        imageBtAnterior = (ImageButton) findViewById(R.id.anterior);
        nomenatela = (TextView) findViewById(R.id.nomenatela);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        barra = (SeekBar) findViewById(R.id.seekBar);
        barra.setVisibility(SeekBar.INVISIBLE);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        nomenatela.setVisibility(TextView.INVISIBLE);
        posicaonalista = 0;
        faz = false;
        sairfora = false;

        /**
         * Da permissão ao aplicação para escrever no cartao sd interno ou externo
         */
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
       // int permissionCheck3= ContextCompat.checkSelfPermission(this, Manifest.permission.R);
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }


        try {
            caminho();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Toca musica
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faz = true;
                tocamedia.tocar();
                MostraBarraTitulo();

            }
        });
        /*
        Botão de pausa da música
         */
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faz = false;
                tocamedia.pausa();
                // progressBar.setVisibility(ProgressBar.INVISIBLE);

            }
        });

        /*
        Botão anterior
         */
        imageBtAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faz.equals(true)) {
                    anterior();
                }
            }
        });

         /*
         Proxima musica
          */
        imageBtproximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faz.equals(true)) {
                    proxima();
                }

            }
        });

        /*
        Sai do sistema
         */
        imageBtSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Tocador.this, "Saindo do Tocador", Toast.LENGTH_SHORT).show();
                tocamedia.parar();
                sairfora = true;
                faz = false;
                Thread.currentThread().interrupt();
                finish();
            }
        });

       /*
       Seleciona a musica da lista
        */
        listaarq.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String musica = (String) parent.getItemAtPosition(position).toString();
                faz = true;
                //tocamedia.abrir(tocamedia.getCamimho() + "/" + musica);

                tabHost.setCurrentTab(1);
                posicaonalista = position;
                tocamedia.abrir(posicaonalista);
                nomenatela.setText(musica + " - Dur. " + CoverteHora(tocamedia.getDuration()));
                MostraBarraTitulo();
                tocamedia.tocar();
            }
        });

        listaarq.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String musica = (String) parent.getItemAtPosition(position).toString();
                tocamedia.abrir(position);
                nomenatela.setText(musica + " - Dur. " + CoverteHora(tocamedia.getDuration()));
                MostraBarraTitulo();
                tocamedia.tocar();
                return false;
            }

        });

        //Posiciona a musica pelo SeekTo na trilha
        barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tocamedia.seekTo(seekBar.getProgress());

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        onRestoreInstanceState(savedInstanceState);

    }


    /**
     * Alimenta a lista de musicas
     */
    public void caminho() throws IOException {
        musicas = tocamedia.listarq();

        // Collections.sort(musicas);
        ArrayAdapter<String> teste1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, musicas);

        listaarq.setAdapter(teste1);
        listaarq.setItemsCanFocus(true);
    }

    /**
     * Metodo da proxima musica
     */
    public void proxima() {
        // Collections.sort(musicas);
        if (sairfora.equals(false)) {
            try {

                if (posicaonalista == listaarq.getCount()) {
                    Toast.makeText(Tocador.this, "Final da lista de músicas voltando ao inicio da lista .", Toast.LENGTH_SHORT).show();
                    posicaonalista = 1; //Seta a posição na lista
                    anterior();
                } else {
                    posicaonalista = posicaonalista + 1;
                    String musica = listaarq.getItemAtPosition(posicaonalista).toString();
                    tocamedia.abrir(posicaonalista);
                    nomenatela.setText(musica + " - Dur. " + CoverteHora(tocamedia.getDuration()));
                    if (tocamedia.naotocando()) {
                        tocamedia.stop();
                    }
                    MostraBarraTitulo();
                    tocamedia.tocar();
                }

            } catch (Exception e) {
                Toast.makeText(Tocador.this, "Final da lista de músicas voltando ao inicio da lista.", Toast.LENGTH_SHORT).show();
                posicaonalista = 1; //Seta a posição na lista
                anterior();
            }
        }

    }

    /**
     * Método da musica posterior
     */
    public void anterior() {
        // Collections.sort(musicas);
        try {
            if (posicaonalista == 0) {
                Toast.makeText(Tocador.this, "Inicio da lista de musica", Toast.LENGTH_SHORT).show();
            } else {
                posicaonalista = posicaonalista - 1;
                String musica = listaarq.getItemAtPosition(posicaonalista).toString();
                tocamedia.abrir(posicaonalista);
                nomenatela.setText(musica + " - Dur. " + CoverteHora(tocamedia.getDuration()));
                MostraBarraTitulo();
                tocamedia.tocar();
            }
        } catch (Exception e) {
            Toast.makeText(Tocador.this, "Inicio da lista de musica", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Thead que atualiza a barra da musica
     */
    public void atualizabarra() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int teste = (tocamedia.getDuration());
                // progressBar.setMax(teste);
                barra.setMax(teste);
                {
                    for (int a = 1; a < teste; a++) {
                        final int finalA = a;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if ((!tocamedia.naotocando()) && sairfora.equals(false) && faz == true) {
                                    Thread.currentThread().interrupt();
                                    proxima();
                                } else {
                                    //progressBar.setProgress(tocamedia.getCurrentPosition());
                                    barra.setProgress(tocamedia.getCurrentPosition());
                                    // Log.v("Teste", "Posicao" + finalA +" Posicao na lista "+posicaonalista);
                                }
                            }
                        });
                        try {

                            if (sairfora.equals(true)) {
                                tocamedia.reset();
                                Thread.currentThread().interrupt();
                            }

                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }).start();
    }

    /**
     * Atualiza a barra e o titulo da musica
     */
    public void MostraBarraTitulo() {
        // progressBar.setVisibility(ProgressBar.VISIBLE);
        nomenatela.setVisibility(TextView.VISIBLE);
        barra.setVisibility(Spinner.VISIBLE);
        atualizabarra();
    }


    /**
     * Coverte msg em horas
     * @param recebe
     * @return
     */
    public String CoverteHora(Integer recebe) {
        Integer segundos = (recebe / 1000) % 60;
        Integer minutos = (recebe / 60000) % 60;
        Integer horas = (recebe / 3600000) % 24;
        String recebe1 = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        return recebe1;
    }

}



