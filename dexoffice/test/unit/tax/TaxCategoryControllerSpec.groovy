package tax



import grails.test.mixin.*
import spock.lang.*

@TestFor(TaxCategoryController)
@Mock(TaxCategory)
class TaxCategoryControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.taxCategoryInstanceList
            model.taxCategoryInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.taxCategoryInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def taxCategory = new TaxCategory()
            taxCategory.validate()
            controller.save(taxCategory)

        then:"The create view is rendered again with the correct model"
            model.taxCategoryInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            taxCategory = new TaxCategory(params)

            controller.save(taxCategory)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/taxCategory/show/1'
            controller.flash.message != null
            TaxCategory.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def taxCategory = new TaxCategory(params)
            controller.show(taxCategory)

        then:"A model is populated containing the domain instance"
            model.taxCategoryInstance == taxCategory
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def taxCategory = new TaxCategory(params)
            controller.edit(taxCategory)

        then:"A model is populated containing the domain instance"
            model.taxCategoryInstance == taxCategory
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/taxCategory/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def taxCategory = new TaxCategory()
            taxCategory.validate()
            controller.update(taxCategory)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.taxCategoryInstance == taxCategory

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            taxCategory = new TaxCategory(params).save(flush: true)
            controller.update(taxCategory)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/taxCategory/show/$taxCategory.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/taxCategory/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def taxCategory = new TaxCategory(params).save(flush: true)

        then:"It exists"
            TaxCategory.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(taxCategory)

        then:"The instance is deleted"
            TaxCategory.count() == 0
            response.redirectedUrl == '/taxCategory/index'
            flash.message != null
    }
}
