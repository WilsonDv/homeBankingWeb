
const app = Vue.createApp({

    data() {
        return {
            ClientAccounts: [],
            transactions: [],
            clientActive: [],
            clientLoans: [],
            accounts: [],
        }
    },

    created() {
        
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');

        axios.get(`/accounts/${id}`)
        .then(response => {
            this.ClientAccounts = response.data
            this.transactions = response.data.transactions
            this.clientLoan = response.data
            this.cortarFecha(this.transactions)
            this.ordenarIdTransaction(this.transactions)
        })
        axios.get('/api/clients/current')
            .then(response => {
            this.clientActive = response.data
            this.clientLoans = response.data.clientLoans
            this.accounts = response.data.accounts
           
        }) 
        .catch(error => {
            return error.message
        })
    },
    methods: {
        cerrar() {
            axios.post('/api/logout')
            .then(response => { 

                window.location.replace("/web/index.html")
             })
             .catch(error => {
                return error.message
            })
                
        },
        
        ordenarIdTransaction(arrayTransactions){
            
            let ordenandoId = arrayTransactions.sort((idA, idB) => {
                if (idA.id > idB.id) { return -1 }
                if (idA.id < idB.id) { return  1 }
                return 0
            })
            return ordenandoId
        },

        enConstruccion() {

            Swal.fire({
                icon: 'info',
                title: 'Oops...',
                text: 'We are working on it, Thank you for your understanding!',
              })
        },

        cortarFecha(arrayTransaction) {
            arrayAux = []
            for (let i = 0; i < arrayTransaction.length; i++) {
                
               const dateNueva =  arrayTransaction[i].date.split('').slice(0, -7).join('') 
                arrayTransaction[i].date = dateNueva
                arrayAux.push(arrayTransaction[i])
           }
           return arrayTransaction

        }
    }//llave methods
   
})

const verAppVue  = app.mount("#app") 