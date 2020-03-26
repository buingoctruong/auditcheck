**Project Description**

    AuditCheck application is tool to get suitable results from the searching process. 
    IBM Watson Discovery service is integrated into this application. 
    With the IBM Watson Discovery service, this application can identify patterns, trends, and 
    insights from structured data and unstructured data to drive better decision making.
    
    This application features:
    
    * Search the best results that are returned by IBM Watson Discovery 
        
    * Upload data to IBM Watson Discovery, those data serve the searching 
        
    * Training data that is uploaded to IBM Watson Discovery 
    
**Workflow**
    
    firstly, you upload all documents in the database to Watson Discovery Service by **Upload** function.
    
    Watson discovery Service gets the documents and starts ingesting (convert, enrich, clean, and normalize),
    store, and query data to extract actionable insights.
    
    At this time, you'll use the Search function to get the results from Watson discovery, and rates each of the results, 
    mark the results as relevant or irrelevant (In this step, training data will be created).
    
    When you're finished rating the documents, you'll use Study function to upload all training data to Watson discovery to train.
    
     Watson discovery will spend 30 minutes for training. when the training process finished, use the Search function 
     and you'll see more suitable results
    

    
**Technology**

    Spring Boot Framework
    Thymeleaf, JPA library
    IBM watson discvovery service
    Java, javascript, HTML

