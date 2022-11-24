package com.exemplorabbitmq.mq.watcher;

import com.exemplorabbitmq.mq.filehandler.FileHandler;
import com.exemplorabbitmq.mq.videoprocessor.VideoProcess;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Watcher para arvore de diretórios.
 */

public class WatchDir extends Thread {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final boolean recursive;
    private final List<String> validExtensions;
    private final Path sourcePathToSave;
    private boolean trace = false;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Registra o diretorio ao WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Registra o diretorio, e todos os sub-diretorios, ao WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // Registra o diretorio e subdiretorios
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Cria um WatchService e registra o directory
     */
    public WatchDir(Path dir, boolean recursive, List<String> validExtensions, Path sourcePathToSave) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.recursive = recursive;
        this.validExtensions = validExtensions;
        this.sourcePathToSave = sourcePathToSave;

        if (recursive) {
            registerAll(dir);
        } else {
            register(dir);
        }

        // Habilita o trace depois do registro inicial
        this.trace = true;
    }

    /**
     * Processa todos os eventos por keys queued ao watcher.
     */
    void processEvents() throws IOException, ParseException {
        for (; ; ) {

            // Aguarda por keys para serem sinalizadas
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // O contexto para o evento de entrada do diretório é o nome do arquivo de entrada
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

//                 System.out.format("%s: %s\n", event.kind().name(), child);

                // Se for um diretorio criado, e for recursivo, então
                // registra e seus sub-diretorios
                if (recursive && (kind == ENTRY_CREATE)) {

                    System.out.format("%s: %s\n", event.kind().name(), child);

                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // Ignore, mantendo o codigo legivel
                    }

                    if(validExtensions.contains(FilenameUtils.getExtension(String.valueOf(child)))) {

                        //REGISTRAR NA FILA PROCESSAR
                        new VideoProcess(child, sourcePathToSave);

                    }
                }
            }

            // reseta a key e, remove do conjunto se o diretorio nao estiver mais acessível
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // Se todos os diretorios estiverem inacessíveis
                if (keys.isEmpty()) {
                    System.out.println("Diretorios inacessíveis.");
                    break;
                }
            }
        }
    }

    public void run() {
        try {
            processEvents();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

}