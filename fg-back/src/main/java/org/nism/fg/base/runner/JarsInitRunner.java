package org.nism.fg.base.runner;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.utils.JarsUtils;
import org.nism.fg.base.utils.SystemUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.sql.SQLException;

@Slf4j
@Component
@AllArgsConstructor
public class JarsInitRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        File jars = FileUtil.file(SystemUtils.getLibsDir());
        JarsWatcher jarsWatcher = new JarsWatcher();
        WatchMonitor monitor = WatchMonitor.create(jars, WatchMonitor.EVENTS_ALL);
        monitor.setWatcher(jarsWatcher);
        monitor.setMaxDepth(1);
        monitor.start();
        log.debug("jar文件扫描启动");
        jarsWatcher.loadJdbc();
    }

    static class JarsWatcher implements Watcher {

        @Override
        public void onCreate(WatchEvent<?> event, Path currentPath) {
            this.loadJdbc();
            log.info("文件监控[创建]: {} -> {}", currentPath, event.context());
        }

        @Override
        public void onModify(WatchEvent<?> event, Path currentPath) {
            this.loadJdbc();
            log.info("文件监控[修改]: {} -> {}", currentPath, event.context());
        }

        @Override
        public void onDelete(WatchEvent<?> event, Path currentPath) {
            this.loadJdbc();
            log.info("文件监控[删除]: {} -> {}", currentPath, event.context());
        }

        @Override
        public void onOverflow(WatchEvent<?> event, Path currentPath) {
            this.loadJdbc();
            log.info("文件监控[覆盖]: {} -> {}", currentPath, event.context());
        }

        public void loadJdbc() {
            try {
                JarsUtils.loadJdbc();
            } catch (SQLException e) {
                log.error("文件监控异常", e);
            }
        }
    }
}
