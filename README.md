# WebHook (Innovative Suggestion)
This feature is part of the Public API. It registers a list of registered listeners and notify them when there are new data available. API will be provided for developers to register / unregister their application URL as listeners

## List all registered webhooks:
GET	BASE_URL/webhooks/all
Will return all webhook listeners.

## Register New Webhooks
POST	BASE_URL/webhooks/subscribe 
Request body (*required):
Subscriber must indicate destination and event type (api) he wants to subscribe. The request will return a new listener. 

## Update Registered Webhooks
POST	BASE_URL/webhooks/update/{id}
Request parameter (*required): id generated when subscribing
Request body (*required):

## Unregister Webhooks
POST	BASE_URL/webhooks/unsubscribe/{id}
Request parameter (*required): id generated when subscribing.
The request will return the unsubscribed listener
