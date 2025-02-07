package com.example.cmpt370project;
/**
 * Subscriber in Model View Controller. Updates whenever the model changes.
 * We have this interface to support the publish-subscribe architecture of MVC.
 */
public interface Subscriber {
    void modelUpdated();
}
