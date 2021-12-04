
const app = Vue.createApp({

    data() {
        return {
            clientAccounts: [],
            clientLoan: [],
            loans: [],
            cuotasPorcentaje: 0,
            montoPorcentaje: 0,
            clientLoans: [],
            boton: false,
            loan: {
                name: '',
                payments: null,
                amount: null,
                percentage: null,
                destinationAccount: '',
            },
            filtrandoCuotas: [],
            cuotasIndividules: [],
        }
    },

    created() {
        
        axios.get('/api/clients/current')
        .then(response => {
        this.clientLoan = response.data
        this.clientAccounts = response.data.accounts.filter( account=> account.accountDelete == false)
        this.clientLoans = response.data.clientLoans

    })
    .catch(error => {
        return error.message
    })
     
    axios.get('/api/loans')
    .then(response => {
       this.loans = response.data
    })
    .catch(error => {
      return error.response.data
    })

    },

    methods: {
         
        consultarLoan(){
            console.log('DisteClick')
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes!'
              }).then((result) => {
                if (result.isConfirmed) {
                  Swal.fire(
                    'Successful!',
                    'Successfully related.',
                    'success'
                  )
                  axios.post('/api/clients/current/loans',{
                    name: this.loan.name,
                    amount: this.loan.amount,
                    payments: this.loan.payments,
                    destinationAccount:this.loan.destinationAccount,
                    })
                    .then(response => {
                        setTimeout(location.reload.bind(location), 3000)
                    })
                    .catch(error => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops... An error occurred in the loan application',
                          })
                        setTimeout(location.reload.bind(location), 3000)
                        return error.response.data
                    })  

                } else {
                    setTimeout(location.reload.bind(location), 500)
                }
            })
        },
        cerrar() {
            axios.post('/api/logout')
            .then(response => {  

                window.location.replace("/web/index.html")
             })
             .catch(error => {
                return error.message
            })
                
        },
        
        enConstruccion() {

            Swal.fire({
                icon: 'info',
                title: 'Oops...',
                text: 'We are working on it, Thank you for your understanding!',
              })
        },

    }, //llave methods 
    computed: {

        filtradoNombrePrestamo(){
            return this.loans.filter(cuota => cuota.name == this.loan.name)   
        },
        calculandoMontoConPorcentaje(){

            if(this.loan.name == "Mortgage"){
               return (this.loan.amount * 1.15).toFixed(2)
            }else if(this.loan.name == "Personal"){
                return (this.loan.amount * 1.20).toFixed(2)
            } else {
                return (this.loan.amount * 1.30).toFixed(2)
            }
            
        },

        filtradoCuotaIndividual(){
           this.cuotasIndividules = this.loans.filter(cuota => cuota.name == this.loan.name).map(cuota => cuota.payments)
            return this.cuotasIndividules[0]
        },
        
        calculandoCuotas(){
              
             if(this.loan.name == "Mortgage"){
                return ((this.loan.amount * 1.15) / this.loan.payments).toFixed(2)
             }else if(this.loan.name == "Personal"){
                return ((this.loan.amount * 1.20) / this.loan.payments).toFixed(2)
             } else {
                return ((this.loan.amount * 1.30) / this.loan.payments).toFixed(2)
             }
             
        },
        desactivarActivarBoton(){
            if( this.loan.name !== '' && this.loan.amount !== null && this.loan.payments  !== null && this.loan.destinationAccount !== '') {

                return false
                       
            } else {
                return true
            }
        },      
    }
})

const verAppVue = app.mount("#app") 