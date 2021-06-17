package com.zcs.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.zcs.elasticsearch.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Test
    public void testCreateIndex() throws Exception {
        CreateIndexRequest request = new CreateIndexRequest("swin_test");
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(createIndexResponse);
    }

    @Test
    public void testExistIndex() throws Exception {
        GetIndexRequest request = new GetIndexRequest("swin_test");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    public void testDeleteIndex() throws Exception {
        DeleteIndexRequest request = new DeleteIndexRequest("swin_test");
        AcknowledgedResponse acknowledgedResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    @Test
    public void testAddDocument() throws IOException {
        User user = new User("swin", 28);

        IndexRequest request = new IndexRequest("swin_test");

        request.id("1");
        request.timeout("1s");

        request.source(JSON.toJSONString(user), XContentType.JSON);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
    }

    @Test
    public void testIsExist() throws IOException {
        GetRequest request = new GetRequest("swin_test", "1");
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    public void testGetDocument() throws IOException {
        GetRequest request = new GetRequest("swin_test", "1");
        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        System.out.println(getResponse.toString());
        System.out.println(getResponse.getSourceAsString());
    }

    @Test
    public void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("swin_test", "1");
        updateRequest.timeout("1s");

        User user = new User("swinUpdate", 28);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
        System.out.println(updateResponse.toString());
    }

    @Test
    public void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("swin_test", "1");
        deleteRequest.timeout("1s");

        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
        System.out.println(deleteResponse.toString());
    }

    @Test
    public void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        List<User> userList = new ArrayList<>();
        userList.add(new User("swin1", 20));
        userList.add(new User("swin2", 22));
        userList.add(new User("swin3", 24));
        userList.add(new User("swin4", 26));
        userList.add(new User("swin5", 28));

        for (int i = 0; i < userList.size(); i++) {
            bulkRequest.add(new IndexRequest("swin_test").source(JSON.toJSONString(userList.get(i)), XContentType.JSON));
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        System.out.println(bulkResponse.hasFailures());

    }


}
