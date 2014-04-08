import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONArray;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SearchServer extends HttpServlet{
    Trie<Integer> trie = null;
    private List<String> data = null;
    private List<Double> lat = null;
    private List<Double> lng = null;
    private int count = 0;
    private Double limit = 500.0;
    private String query = null;
    private Collection<Integer> values = null;
    public SearchServer() throws IOException {
        super();
        trie = new Trie<Integer>();
        data = new ArrayList<String>();
        lat = new ArrayList<Double>();
        lng = new ArrayList<Double>();
        long start = System.currentTimeMillis();
        String dataPath = "F:/Compiler/Java/workspace/TypeaheadSearch/data/zipcode-address.json";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataPath)));
        String line = null;
        Pattern p = Pattern.compile("\"latlng\" : . (.*?), (.*?) ., \"name\" : \"(.*?)\"");
        Matcher m = null;
        while ((line = reader.readLine()) != null) {
            m = p.matcher(line);
            if (m.find()) {
                data.add(line);
                lat.add(Double.valueOf(m.group(1)) * 1000);
                lng.add(Double.valueOf(m.group(2)) * 1000);
                trie.put(m.group(3).toLowerCase(), count++);
            }
        }
        reader.close();
        System.out.println("File read time spended: " + (System.currentTimeMillis() - start) + "ms");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        String pquery = request.getParameter("query");
        Double plat = Double.valueOf(request.getParameter("lat")) * 1000;
        Double plng = Double.valueOf(request.getParameter("lng")) * 1000;
        if (pquery == null) {
            System.out.println("null query");
        } else {
            long start = System.currentTimeMillis();
            PrintWriter out = response.getWriter();
            List<String> result = new ArrayList<String>();
            pquery = new String(pquery.getBytes("ISO-8859-1"), "utf-8").toLowerCase();
            if (!pquery.equals(query)) {
                query = pquery;
                values = trie.search(pquery).values();
            }
            for (Integer v : values) {
                if ((lat.get(v) - plat) * (lat.get(v) - plat) + (lng.get(v) - plng) * (lng.get(v) - plng) < limit) {
                    result.add(data.get(v));
                    if (result.size() == 10)
                        break;
                }
            }
            out.print(JSONArray.fromObject(result));
            out.flush();
            out.close();
            System.out.println("Search time spended: " + (System.currentTimeMillis() - start) + "ms" + " with " + query);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
