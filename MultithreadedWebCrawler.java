import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadedWebCrawler {

    // Shared data structures
    private final Queue<String> urlQueue = new LinkedList<>(); // Queue of URLs to crawl
    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>()); // Track visited URLs
    private final ExecutorService threadPool; // Thread pool for concurrent crawling

    // Constructor
    public MultithreadedWebCrawler(int threadPoolSize) {
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    // Method to start crawling
    public void startCrawling(String seedUrl) {
        urlQueue.add(seedUrl); // Add the seed URL to the queue
        visitedUrls.add(seedUrl); // Mark the seed URL as visited

        while (!urlQueue.isEmpty()) {
            String url = urlQueue.poll(); // Get the next URL to crawl
            threadPool.submit(() -> crawlUrl(url)); // Submit the URL to the thread pool
        }

        shutdownThreadPool(); // Shutdown the thread pool after all URLs are processed
    }

    // Method to crawl a single URL
    private void crawlUrl(String url) {
        try {
            // Fetch the webpage content
            String webpageContent = fetchWebpageContent(url);
            System.out.println("Crawled: " + url);

            // Process the webpage content (e.g., extract links)
            Set<String> links = extractLinks(webpageContent);

            // Add new links to the queue
            for (String link : links) {
                if (!visitedUrls.contains(link)) {
                    visitedUrls.add(link); // Mark the link as visited
                    urlQueue.add(link); // Add the link to the queue
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error crawling " + url + ": " + e.getMessage());
        }
    }

    // Method to fetch webpage content
    private String fetchWebpageContent(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Method to extract links from webpage content (dummy implementation)
    private Set<String> extractLinks(String webpageContent) {
        // In a real implementation, use an HTML parser like Jsoup to extract links
        Set<String> links = new HashSet<>();
        // Example: Extract links from a simple HTML string
        String[] tokens = webpageContent.split(" ");
        for (String token : tokens) {
            if (token.startsWith("http://") || token.startsWith("https://")) {
                links.add(token);
            }
        }
        return links;
    }

    // Method to shutdown the thread pool
    private void shutdownThreadPool() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }

    // Main method
    public static void main(String[] args) {
        // Seed URL to start crawling
        String seedUrl = "https://example.com";

        // Create a web crawler with a thread pool of size 5
        MultithreadedWebCrawler crawler = new MultithreadedWebCrawler(5);

        // Start crawling
        crawler.startCrawling(seedUrl);
    }
}