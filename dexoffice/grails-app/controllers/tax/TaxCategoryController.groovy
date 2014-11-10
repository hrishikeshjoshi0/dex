package tax



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TaxCategoryController {

	static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TaxCategory.list(params), model:[taxCategoryInstanceCount: TaxCategory.count()]
    }

    def show(TaxCategory taxCategoryInstance) {
        respond taxCategoryInstance
    }

    def create() {
        respond new TaxCategory(params)
    }

    @Transactional
    def save(TaxCategory taxCategoryInstance) {
        if (taxCategoryInstance == null) {
            notFound()
            return
        }

        if (taxCategoryInstance.hasErrors()) {
            respond taxCategoryInstance.errors, view:'create'
            return
        }

        taxCategoryInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'taxCategory.label', default: 'TaxCategory'), taxCategoryInstance.id])
                redirect taxCategoryInstance
            }
            '*' { respond taxCategoryInstance, [status: CREATED] }
        }
    }

    def edit(TaxCategory taxCategoryInstance) {
        respond taxCategoryInstance
    }

    @Transactional
    def update(TaxCategory taxCategoryInstance) {
        if (taxCategoryInstance == null) {
            notFound()
            return
        }

        if (taxCategoryInstance.hasErrors()) {
            respond taxCategoryInstance.errors, view:'edit'
            return
        }

        taxCategoryInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TaxCategory.label', default: 'TaxCategory'), taxCategoryInstance.id])
                redirect taxCategoryInstance
            }
            '*'{ respond taxCategoryInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(TaxCategory taxCategoryInstance) {

        if (taxCategoryInstance == null) {
            notFound()
            return
        }

        taxCategoryInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TaxCategory.label', default: 'TaxCategory'), taxCategoryInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxCategory.label', default: 'TaxCategory'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
