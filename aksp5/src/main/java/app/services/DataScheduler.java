package app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import app.models.University;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ManagedResource(
        objectName = "Practice23MBeans:category=MBeans,name=DataScheduler"
)
public class DataScheduler {
    private final UniversityService universityService;
    private final StudentService studentService;
    @Value("${dir.result}")
    private String path;

    @Scheduled(cron = "0 0/30 * * * *")
    @ManagedOperation(
            description = "Deletes all contents of the directory and " +
                    "writes all data from the database to it every 30 minutes"
    )
    public void start() throws IOException, NullPointerException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        log.info("SCHEDULED task started at {}", formatter.format(date));

        File dir = ResourceUtils.getFile(path);
        String absPath = dir.getAbsolutePath();
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(file -> {
            if (file.isFile()) {
                log.info("File " + file.getName() + " was deleted: " + file.delete());
            }
        });

        BufferedWriter gBufWriter = createWriter(absPath, "universities.txt");
        gBufWriter.write("id|university_name|creation_Date\n");
        universityService.getAllUniversities()
                .forEach(group -> {
                            try {
                                gBufWriter.write(
                                        String.format(
                                                "%d|%s|%s\n",
                                                group.getId(),
                                                group.getUniversityName(),
                                                group.getCreationDate()
                                        )
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
        gBufWriter.close();

        BufferedWriter sBufWriter = createWriter(absPath, "students.txt");
        sBufWriter.write("id|university_id|first_name|last_name|middle_name\n");
        studentService.getAllStudents()
                .forEach(student -> {
                            try {
                                sBufWriter.write(
                                        String.format(
                                                "%d|%d|%s|%s|%s\n",
                                                student.getId(),
                                                student.getUniversity().getId(),
                                                student.getFirstName(),
                                                student.getLastName(),
                                                student.getMiddleName()
                                        )
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
        sBufWriter.close();

        log.info("SCHEDULED task finished.");
    }

    private BufferedWriter createWriter(String dir, String filename) throws IOException {
        return new BufferedWriter(new FileWriter(String.format("%s/%s", dir, filename)));
    }
}