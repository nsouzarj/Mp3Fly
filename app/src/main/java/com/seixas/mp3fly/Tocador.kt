/**
 * Projeto de MP3
 * Autor: Nelson Seixas
 */
package com.seixas.mp3fly


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TabHost.TabSpec
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import kotlin.system.exitProcess

class Tocador : AppCompatActivity() {
    private var imageBtproximo: ImageButton? = null
    private var imageButton2: ImageButton? = null
    private var imageButton3: ImageButton? = null
    private var imageBtSair: ImageButton? = null
    private var imageBtAnterior: ImageButton? = null
    private var posicaonalista: Int? = null
    private val btsdcad: Button? = null
    private val btsdExterno: Button? = null
    private var tabHost: TabHost? = null
    private val botaoabrir1: Button? = null
    private var tabSpec: TabSpec? = null
    private var listaarq: ListView? = null
    private val view: View? = null
    private var textView: TextView? = null
    private var nomenatela: TextView? = null
    private var duracao: TextView? = null
    private var musicas: List<String?>? = null
    private val caminhocompleto: String? = null
    private var nomedamusica: TextView? = null
    private var tocamedia: Tocamedia? = null
    private var progressBar: ProgressBar? = null
    private val mHandler = Handler()
    private var faz: Boolean? = null
    private var sairfora: Boolean? = null
    private var barra: SeekBar? = null
    private var relogio: TextView? = null
    private var imglista: ImageView? = null
    private var teste: ArrayList<View>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tocamedia = Tocamedia()

        setContentView(R.layout.activity_tocador)



        tabHost = findViewById<View>(R.id.tabHost1) as TabHost
        tabHost!!.setup()
        tabHost!!.currentTab = 0
        tabSpec = tabHost!!.newTabSpec("Aba1")
        tabSpec!!.setContent(R.id.lista)
        tabSpec!!.setIndicator("Musicas")
        tabHost!!.addTab(tabSpec)
        tabSpec = tabHost!!.newTabSpec("Aba2")
        tabSpec!!.setContent(R.id.musica)
        tabSpec!!.setIndicator("Play")
        tabHost!!.addTab(tabSpec)
        imageBtSair = findViewById<View>(R.id.imageBtSair) as ImageButton
        listaarq = findViewById<View>(R.id.listademusicas) as ListView
        textView = findViewById<View>(R.id.nomedamusica) as TextView
        imageButton2 = findViewById<View>(R.id.player) as ImageButton
        imageButton3 = findViewById<View>(R.id.pausa) as ImageButton
        imageBtproximo = findViewById<View>(R.id.proximo) as ImageButton
        imageBtAnterior = findViewById<View>(R.id.anterior) as ImageButton
        nomenatela = findViewById<View>(R.id.nomenatela) as TextView
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        duracao = findViewById<View>(R.id.duracao) as TextView
        nomedamusica = findViewById<View>(R.id.nomedamusica) as TextView
        barra = findViewById<View>(R.id.seekBar) as SeekBar
        relogio = findViewById<View>(R.id.contador) as TextView
        imglista = findViewById<View>(R.id.image2) as ImageView
        barra!!.visibility = SeekBar.INVISIBLE
        progressBar!!.visibility = ProgressBar.INVISIBLE
        nomenatela!!.visibility = TextView.INVISIBLE
        posicaonalista = 0
        faz = false
        sairfora = false




        if (minhainstancia != null) {
            println("JA esta criado")

        } else {
            onDestroy()
        }

        /**
         * Da permissão ao aplicação para escrever no cartao sd interno ou externo
         */
        val permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        // int permissionCheck3= ContextCompat.checkSelfPermission(this, Manifest.permission.R);
        //ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_WRITE_STORAGE)
        }


        try {

            caminho()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Toca musica
        imageButton2!!.setOnClickListener {
            faz = true

            tocamedia!!.tocar()

            MostraBarraTitulo()
        }
        /*
        Botão de pausa da música
         */imageButton3!!.setOnClickListener {
            faz = false
            Toast.makeText(this, "Pausa na música", Toast.LENGTH_SHORT).show()
            tocamedia!!.pausa()
            // progressBar.setVisibility(ProgressBar.INVISIBLE);
        }

        /*
        Botão anterior
         */imageBtAnterior!!.setOnClickListener {
            if (faz == true) {
                Toast.makeText(this, "Música anterior", Toast.LENGTH_SHORT).show()
                anterior()
            }
        }

        /*
         Proxima musica
          */imageBtproximo!!.setOnClickListener {
            if (faz == true) {
                Toast.makeText(this, "Próxima música", Toast.LENGTH_SHORT).show()
                proxima()
            }
        }

        /*
        Sai do sistema
         */imageBtSair!!.setOnClickListener {
            Toast.makeText(this@Tocador, "Saindo do Tocador", Toast.LENGTH_SHORT).show()
            tocamedia!!.parar()
            sairfora = true
            faz = false
            Thread.sleep(200)
            Thread.currentThread().interrupt()
            finish()
            tocamedia = null
            closeContextMenu()
            exitProcess(0)


        }

        /*
       Seleciona a musica da lista

        */listaarq!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            faz = true
            tabHost!!.currentTab = 1
            nomedamusica!!.setTextColor(Color.BLUE)
            posicaonalista = position
            tocamedia!!.abrir(posicaonalista)
            nomenatela!!.text = parent.getItemAtPosition(position).toString()
            duracao!!.text = "Duração :" + CoverteHora(tocamedia!!.duration)
            MostraBarraTitulo()
            tocamedia!!.tocar()
        }
        listaarq!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            tocamedia!!.abrir(position)
            nomedamusica!!.setTextColor(Color.BLUE)
            nomenatela!!.text = parent.getItemAtPosition(position).toString()
            duracao!!.text = "Duração :" + CoverteHora(tocamedia!!.duration)
            MostraBarraTitulo()
            tocamedia!!.tocar()
            false
        }

        //Posiciona a musica pelo SeekTo na trilha
        barra!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                tocamedia!!.seekTo(seekBar.progress)
            }
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        onRestoreInstanceState(savedInstanceState)
    }

    /**
     * Alimenta a lista de musicas
     */
    @Throws(IOException::class)
    fun caminho() {
        musicas = tocamedia?.listarq()

        // Collections.sort(musicas);
        //val teste1 = ArrayAdapter(this,  android.R.layout.simple_list_item_1,  musicas!!)
        val teste1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, musicas!!)
        listaarq!!.adapter = teste1
        listaarq!!.itemsCanFocus = true

    }

    /**
     * Metodo da proxima musica
     */
    fun proxima() {
        // Collections.sort(musicas);
        if (sairfora == false) {
            try {
                if (posicaonalista == listaarq!!.count) {
                    Toast.makeText(this@Tocador, "Final da lista de músicas voltando ao inicio da lista .", Toast.LENGTH_SHORT).show()
                    posicaonalista = 1 //Seta a posição na lista
                    anterior()
                } else {
                    posicaonalista = posicaonalista!! + 1
                    val musica = listaarq!!.getItemAtPosition(posicaonalista!!).toString()
                    tocamedia!!.abrir(posicaonalista)
                    nomenatela!!.text = musica
                    duracao!!.text = "Duração :" + CoverteHora(tocamedia!!.duration)
                    if (tocamedia!!.naotocando()) {
                        tocamedia!!.stop()
                    }
                    nomedamusica!!.setTextColor(Color.parseColor("blue"))
                    MostraBarraTitulo()
                    tocamedia!!.tocar()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Tocador, "Final da lista de músicas voltando ao inicio da lista.", Toast.LENGTH_SHORT).show()
                posicaonalista = 1 //Seta a posição na lista
                anterior()
            }
        }
    }

    /**
     * Método da musica posterior
     */
    fun anterior() {
        // Collections.sort(musicas);
        try {
            if (posicaonalista == 0) {
                Toast.makeText(this@Tocador, "Inicio da lista de musica", Toast.LENGTH_SHORT).show()
            } else {
                posicaonalista = posicaonalista!! - 1
                val musica = listaarq!!.getItemAtPosition(posicaonalista!!).toString()
                tocamedia!!.abrir(posicaonalista)
                nomenatela!!.text = musica
                duracao!!.text = "Duraçao :" + CoverteHora(tocamedia!!.duration)
                MostraBarraTitulo()
                tocamedia!!.tocar()
            }
        } catch (e: Exception) {
            Toast.makeText(this@Tocador, "Inicio da lista de musica", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    Thead que atualiza a barra da musica
     */
    fun atualizabarra() {
        Thread(object : Runnable {
            override fun run() {
                val teste = tocamedia!!.duration
                // progressBar.setMax(teste);
                barra!!.max = teste
                run {
                    for (a in 1 until teste) {
                        val finalA = a
                        mHandler.post {
                            if (!tocamedia!!.naotocando() && sairfora == false && faz == true) {
                                Thread.currentThread().interrupt()
                                proxima()
                            } else {
                                //progressBar.setProgress(tocamedia.getCurrentPosition());
                                barra!!.progress = tocamedia!!.currentPosition
                                relogio!!.text = "Contagem :" + CoverteHora((tocamedia!!.currentPosition - tocamedia!!.duration) * (-1))
                                // Log.v("Teste", "Posicao" + finalA +" Posicao na lista "+posicaonalista);
                                nomedamusica!!.setTextColor(Color.parseColor("blue"))
                            }
                        }
                        try {
                            if (sairfora == true) {
                                tocamedia!!.reset()
                                Thread.currentThread().interrupt()

                            }

                            if (!Thread.currentThread().isInterrupted) {
                                Thread.sleep(200)
                            }

                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }

                }
            }
        }).start()
    }

    /**
     * Atualiza a barra e o titulo da musica
     */
    fun MostraBarraTitulo() {
        // progressBar.setVisibility(ProgressBar.VISIBLE);
        nomenatela!!.visibility = TextView.VISIBLE
        barra!!.visibility = Spinner.VISIBLE
        atualizabarra()
        nomedamusica!!.setTextColor(Color.parseColor("blue"))
    }


    /**
     * Coverte msg em horas
     * @param recebe
     * @return
     */
    fun CoverteHora(recebe: Int): String {
        val segundos = (recebe / 1000 % 60)
        val minutos = (recebe / 60000 % 60)
        val horas = (recebe / 3600000 % 24)
        return String.format("%02d:%02d:%02d", horas, minutos, segundos)
    }

    companion object {
        private const val REQUEST_WRITE_STORAGE = 112
        private var INSTANCIA: Tocador? = null
        val minhainstancia: Tocador
            get() {
                if (INSTANCIA == null) {
                    INSTANCIA = Tocador()

                }
                return INSTANCIA!!
            }


    }
}