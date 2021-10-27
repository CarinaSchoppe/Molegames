import java.io.*;

class Main {
    public static void main(String[] args) {
        try {

            addToFile("MAX_PLAYERS", 30);
            addToFile("MAX_ENEMIES", 32);
            addToFile("MAX_TANKS", 33);
            addToFile("MAX_MAUL", 34);
            readFromFile("MAX_ENEMIES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addToFile(String opt,int value) throws IOException {
        BufferedWriter bw = new BufferedWriter(
                new FileWriter("config.txt", true));
        bw.write(opt + " - " + String.valueOf(value) + "\n");
        bw.close();
    }
    private static int readFromFile(String opt) throws IOException {
        BufferedReader br = new BufferedReader(
                new FileReader("config.txt"));
        String line;
        while((line = br.readLine()) != null){
            if(opt.equals(line.split(" - ")[0])){
                System.out.print(Integer.parseInt((line.split(" - ")[1])));
                return(Integer.parseInt((line.split(" - ")[1])));
            }
        }
        return 0;
    }

}


