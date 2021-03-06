package io.pivotal.pal.tracker.allocations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestOperations;

public class ProjectClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RestOperations restOperations;
    private final String endpoint;
    private final CacheManager cacheManager;
    //private Map<Long, ProjectInfo> projectsCache = new ConcurrentHashMap<>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint, CacheManager cacheManager) {
        this.restOperations= restOperations;
        this.endpoint = registrationServerEndpoint;
        this.cacheManager = cacheManager;
    }

    @Cacheable("projects")
    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo project = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        //projectsCache.put(projectId, project);
        return project;
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        logger.info("Getting project with id {} from cache", projectId);
        //return projectsCache.get(projectId);
        return (ProjectInfo) cacheManager.getCache("projects").get(projectId);
    }
}
