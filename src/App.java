import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import dominio.Album;
import dominio.Artista;
import dominio.Musica;

public class App {
    public static void main(String[] args) throws Exception {

        // Criando músicas
        Musica musica1 = new Musica();
        musica1.setNome("Californication");
        musica1.setDuracao(120);
        musica1.setArquivoAudio("./assets/Red-Hot-Chili-Peppers-Californication.wav");
        musica1.setGenero("Rock");

        Musica musica2 = new Musica();
        musica2.setNome("Otherside");
        musica2.setDuracao(120);
        musica2.setArquivoAudio("./assets/Red-Hot-Chili-Peppers-Otherside.wav");
        musica2.setGenero("Rock");

        // Criando álbum
        Album album1 = new Album();
        album1.setNome("Primeiro album");
        album1.setAno(2000);
        album1.addMusica(musica1);
        album1.addMusica(musica2);

        // Criando artista
        Artista redHot = new Artista();
        redHot.setNome("Red Hot Chili Peppers");
        redHot.addAlbum(album1);

        System.out.println("Abrindo o PlayMusic");

        // Inicializando player de áudio
        AudioPlayer player = new AudioPlayer();
        final int[] currentMusicIndex = { 0 }; // Índice da música atual

        // Função para carregar e tocar uma música
        Runnable loadAndPlay = () -> {
            player.stopAudio(); // Para a música atual
            String arquivoAudio = redHot.getAlbuns().get(0).getMusicas().get(currentMusicIndex[0]).getArquivoAudio();
            player.loadAudio(arquivoAudio); // Carrega o novo áudio
            player.playAudio(); // Toca o novo áudio
        };
        
        // Botão Play/Stop
        JButton playStopButton = new JButton("Play");
        playStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player.isPlaying) {
                    player.playAudio();
                    playStopButton.setText("Stop");
                } else {
                    player.stopAudio();
                    playStopButton.setText("Play");
                }
            }
        });
        
        ImageIcon icon = new ImageIcon("./assets/music.png");

        // Botão Próxima música
        JButton nextButton = new JButton("proximo");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMusicIndex[0] = (currentMusicIndex[0] + 1)
                        % redHot.getAlbuns().get(0).getMusicas().size();
                playStopButton.setText("Stop");
                loadAndPlay.run();
            }
        });

        // Botão Música anterior
        JButton prevButton = new JButton("anterior");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMusicIndex[0] = (currentMusicIndex[0] - 1 + redHot.getAlbuns().get(0).getMusicas().size())
                        % redHot.getAlbuns().get(0).getMusicas().size();
                playStopButton.setText("Stop");
                loadAndPlay.run();
            }
        });


        // Exibe um JOptionPane com os botões
        JOptionPane.showOptionDialog(
                null,
                redHot.getAlbuns().get(0).getMusicas().get(currentMusicIndex[0]).getNome(),
                "PlayMusic",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                icon,
                new Object[] { prevButton, playStopButton, nextButton }, playStopButton);

        // Fecha o clip de áudio ao encerrar o programa
        if (player.audioClip != null) {
            player.audioClip.close();
        }
    }
}
