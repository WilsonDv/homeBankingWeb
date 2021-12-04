const app = Vue.createApp({

    data() {
        return {
            clients: [],
            clientJson: [],
            newClient: ["", "", ""],
            name: '',
            maxAmount :0, 
            percentage : 0,
            payments : [],
            paymentSelected: '',
            percentage: 0,
        }
    },

    created() {
        
        this.loadData()

    },
    methods: {

        loadData(){

            axios.get('/rest/clients')
            .then(response => {
            this.clientJson = response.data
            this.clients = response.data._embedded.clients
            })
            .catch(error => {
                console.log(error.message);
            })
        },

        addLoan(){

            let convertir =  (this.percentage / 100) + 1
            this.percentage = convertir
            this.payments.push(this.paymentSelected)
            axios.post('/api/admin/loan', {
                "name": this.name,
                "maxAmount": this.maxAmount,
                "percentage": this.percentage,
                "payments": this.payments,
            } )
                .then(response => {
                 location.reload()
                })

            .catch(error => {
                console.log(error.message);
            })
        },

        addClient(){
            let clientCapForm = {

                firstName: this.newClient[0],
                lastName: this.newClient[1],
                email: this.newClient[2],
            }

            this.postClient(clientCapForm);

        },

        postClient(clientCapForm){
            axios.post('/rest/clients', clientCapForm)
                .then(response => {
                this.newClient.forEach(element => {
                    element = ""
                })
                this.loadData()
            })
            .catch(error => {
                console.log(error.message);
            })

        },
    },
    computed: {
        desabilitarYhabilitarBtn(){
            if(this.newClient.every(element=> element !== "")){
                return false
            } else {
                return true
            }
        },
    }
})

app.mount("#app") 
