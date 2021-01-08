package Uility;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Csv {
    private StringBuilder sb = new StringBuilder();
    private File file;

    public Csv add(String string)
    {
        sb.append(string).append(",");
        return this;
    }

    public Csv add(int string)
    {
        sb.append(string).append(",");
        return this;
    }

    public Csv add(double string)
    {
        sb.append(string).append(",");
        return this;
    }

    public Csv addBreak()
    {
        sb.append(System.getProperty("line.separator"));
        return this;

    }

    public File write(String name) {
        file = new File(name+ ".csv");
        try (PrintWriter writer = new PrintWriter(file )) {
            writer.write(sb.toString());
        } catch (IOException e) {

        }
        return file;
    }

    public void removeFile()
    {
        file.delete();
        file = null;
    }
}
