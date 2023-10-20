import java.io.*;
import java.util.*;

public class AalInterpreter {

    public static void main(String[] args) throws Exception {
        String filePath = "sample.aal";
        interpret(filePath);
    }

    public static void interpret(String filePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder javaCode = new StringBuilder();

        javaCode.append("import android.app.Activity;\n");
        javaCode.append("import android.os.Bundle;\n");
        javaCode.append("import android.widget.*;\n");
        javaCode.append("import android.view.*;\n");
        javaCode.append("\npublic class GeneratedActivity extends Activity {\n\n");
        javaCode.append("@Override\n");
        javaCode.append("protected void onCreate(Bundle savedInstanceState) {\n");
        javaCode.append("super.onCreate(savedInstanceState);\n");
        javaCode.append("LinearLayout layout = new LinearLayout(this);\n");
        javaCode.append("layout.setOrientation(LinearLayout.VERTICAL);\n");

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("create button:")) {
                String buttonText = line.split(":")[1].trim();
                javaCode.append("Button button = new Button(this);\n");
                javaCode.append("button.setText(\"").append(buttonText).append("\");\n");
                javaCode.append("layout.addView(button);\n");
            } else if (line.startsWith("create textview:")) {
                String textViewText = line.split(":")[1].trim();
                javaCode.append("TextView textView = new TextView(this);\n");
                javaCode.append("textView.setText(\"").append(textViewText).append("\");\n");
                javaCode.append("layout.addView(textView);\n");
            } else if (line.startsWith("on button click:")) {
                String action = line.split(":")[1].trim();
                if (action.startsWith("Display")) {
                    String message = action.split("\"")[1];
                    javaCode.append("button.setOnClickListener(new View.OnClickListener() {\n");
                    javaCode.append("@Override\n");
                    javaCode.append("public void onClick(View v) {\n");
                    javaCode.append("Toast.makeText(GeneratedActivity.this, \"")
                           .append(message).append("\", Toast.LENGTH_SHORT).show();\n");
                    javaCode.append("}\n");
                    javaCode.append("});\n");
                }
            } else if (line.startsWith("import java:")) {
                String javaImport = line.split(":")[1].trim();
                javaCode.append("import ").append(javaImport).append(";\n");
            }
        }

        javaCode.append("setContentView(layout);\n");
        javaCode.append("}\n");
        javaCode.append("}\n");

        System.out.println(javaCode.toString());
        reader.close();
    }
}
