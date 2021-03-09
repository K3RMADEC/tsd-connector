//package com.rgallego.twitteranalyzer.connector;
//
//import com.rgallego.twitteranalyzer.connector.bean.DeleteRule;
//import com.rgallego.twitteranalyzer.connector.bean.Rule;
//import com.rgallego.twitteranalyzer.connector.bean.Rules;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpMethod;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import reactor.core.Disposable;
//import reactor.core.publisher.Flux;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//class TwitterAnalyzerConnectorTests {
//
//    @Autowired
//    TwitterRestConnector twitterRestConnector;
//
//    @Test
//    public void sampledTest() {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("expansions", "author_id");
//		params.add("tweet.fields", "created_at");
//		params.add("user.fields", "created_at");
//        Flux<String> flux = twitterRestConnector.getFluxResponse("/2/tweets/sample/stream", HttpMethod.GET, params);
//
//        System.out.println(flux.next().block());
//
//    }
//
//    @Test
//    public void createRuleTest() {
//        List<Rule> ruleList = new ArrayList<>();
//        Rule rule = new Rule("from:ProjectTsd", "from ProjectTsd");
//        ruleList.add(rule);
//        Rules rules = new Rules();
//        rules.setAdd(ruleList);
//        String tweet = twitterRestConnector.createStreamRules("/2/tweets/search/stream/rules", HttpMethod.POST, rules);
//        System.out.println(tweet);
//
//    }
//
//    @Test
//    public void deleteRulesTest() {
//        List<String> ids = new ArrayList<>();
//        ids.add("1309526302391767041");
//        Rules rules = new Rules();
//        rules.setDelete(new DeleteRule(ids));
//        String tweet = twitterRestConnector.createStreamRules("/2/tweets/search/stream/rules", HttpMethod.POST, rules);
//        System.out.println(tweet);
//
//    }
//
//    @Test
//    public void getCreatedRulesTest() {
//        String response = twitterRestConnector.getCreatedRules("/2/tweets/search/stream/rules", HttpMethod.GET);
//        System.out.println(response);
//    }
//
//    @Test
//    public void filteredTest() {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("expansions", "author_id,geo.place_id");
//        params.add("tweet.fields", "created_at,lang,entities,source,context_annotations");
//        params.add("user.fields", "created_at");
//
//        Flux<String> flux = twitterRestConnector.getFluxResponse("/2/tweets/search/stream", HttpMethod.GET, params);
//
////        System.out.println(flux.next().block());
//        Disposable disposable = flux.subscribe(System.out::println);
//        disposable.dispose(); // sirve para parar el stream
//    }
//
//}
