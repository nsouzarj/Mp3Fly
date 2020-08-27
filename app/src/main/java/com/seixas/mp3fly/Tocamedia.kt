package com.seixas.mp3fly

import android.content.Context
import android.media.Image
import android.media.MediaPlayer
import android.os.Environment
import android.os.storage.StorageManager
import android.widget.ImageView
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by nelson on 04/04/2016.
 */
open class Tocamedia : MediaPlayer() {
    private val context: Context? = null
    private val cartao = Environment.getExternalStorageDirectory()
    private val listademusicas: List<String>? = null
    private val minhaLista: List<String>? = null
    private val fileFilter: FileFilter? = null
    private val novoFilter: FileFilter? = null
    private val nomedir: FileFilter? = null
    private var camimho: String? = null
    private var duracao: Int? = null
    private var soamusica: List<String>? = null
    private var musicas: List<String>? = null
    private val diretorio: List<String>? = null
    private val arqs: Array<String>? = null
    private val storageManager: StorageManager? = null
    private val environment: Environment? = null
    private val nomediretorio: Array<String>? = null

    var imagem: List<Image>? = null
    var dirraiz:String?=""
    var foto = R.id.image2
    //  val novocaminho=""
    fun tocar() {
        if (isPlaying) {
            stop() //Para a musica antes
        }
        start()
    }

    fun pausa() {
        pause()
    }

    fun proxima(triha: Int?) {
        //  selectTrack(triha);
    }

    fun parar() {

        stop()
    }

    fun tempodamusica(): Int {
        duracao = if (isPlaying) {
            duration
        } else {
            0
        }
        return duracao as Int
    }

    fun posicao(): Int {
        return currentPosition
    }

    fun abrir(musica: Int?) {
        try {
            if (isPlaying) {
                stop() //Para a musica antes se estiver tocando
            }
            // musicas.contains(musica);
            reset()
            val teste =dirraiz+'/'+ soamusica!![musica!!]
            setDataSource(teste)
            prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /*
     Verifica se esta tocando
      */
    fun naotocando(): Boolean {
        return isPlaying
    }

    //Lista os arquvios
    @Throws(IOException::class)
    fun listarq(): List<String?> {

        soamusica = ArrayList()
        musicas = ArrayList()
        imagem = ArrayList()

        val directotio = Environment.getExternalStorageDirectory()
        val internaldisk0 = File("/storage")
        val internaldisk1 = File(Environment.DIRECTORY_MUSIC+"/").absolutePath

        //Procura sempre nas pasta de musicas
        val cartao1 =  File("/storage/").listFiles()
        val diretorio = File(directotio.absolutePath).listFiles()

        for (file in cartao1) {
            if (file === cartao1[0])
            {
                val musica = File(file.absolutePath + "/MUSICAS/").listFiles()
                val musicaintera =  File(file.absolutePath + "/Music/").listFiles()

                if (musica != null) {
                for (file1 in musica) {

                  (musicas as ArrayList<String?>).add(file1.absolutePath)
                  (soamusica as ArrayList<String?>).add(file1.name)
                }
                }

                if (musicaintera != null) {
                    //Pega o caminho Asol
                    this.dirraiz =file.absolutePath+'/'+Environment.DIRECTORY_MUSIC
                    for (file1Externo in musicaintera) {

                        (musicas as ArrayList<String?>).add(file1Externo.absolutePath)
                        (soamusica as ArrayList<String?>).add(file1Externo.name)
                    }


                }

            } else if (file === cartao1[1]) {
                val sd1 = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath).listFiles()
                if (sd1 != null) {
                    for (file2 in sd1) {

                        (musicas as ArrayList<String?>).add(file2.absolutePath)
                        (soamusica as ArrayList<String?>).add(file2.name)
                    }
                }
            } else if (file === cartao1[2]) {
                val sd2 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath).listFiles()
                if (sd2 != null) {
                    for (file3 in sd2) { //if (file1.getAbsolutePath().equals("/MUSICA/"))
                        (musicas as ArrayList<String?>).add(file3.absolutePath)
                        (soamusica as ArrayList<String?>).add(file3.name)
                    }
                }
            }

        }


        /*
        * Pega as pastas do crtao interno e externo
         */
       // val storage = internaldisk1
        //final File storage= new File(Audio.Media.IS_MUSIC);


        //Se for um diretorio d emusica adciona na lista
        /**
         * Faz o filtro
         */
        val fileNameFilter = FilenameFilter { dir, name ->
            if (name.lastIndexOf('.') > 0) {
                // get last index for '.' char
                val lastIndex = name.lastIndexOf('.')

                // get extension
                val str = name.substring(lastIndex)

                // match path name extension
                if (str == ".mp3") {
                    return@FilenameFilter true
                }
            }
            false
        }



        //val indice=Collections.indexOfSubList(musicas, soamusica)
         Collections.sort( soamusica)
        return   soamusica as  ArrayList<String?>
    }

}