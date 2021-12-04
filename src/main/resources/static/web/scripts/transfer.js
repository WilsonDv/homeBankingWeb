const app = Vue.createApp({

    data() {
        return {
            clientAccounts: [],
            clientTransactions: [],
            diferentAcoounstTransfer: [],
            clientLoans: [],
            boton: false,
            transferencia: {
                operationType:'',
                originAccount: '',
                destinationAccount: '',
                amount:'',
                description: '',
            },
        }
    },

    created() {
        
        axios.get('/api/clients/current')
        .then(response => {
            this.clientTransactions = response.data
            this.clientAccounts = response.data.accounts.filter( account=> account.accountDelete == false)
            this.clientLoans = response.data.clientLoans
    })
    .catch(error => {
        console.log(error.message);
    })

    },
    methods: {

        enviarCamposTransfer(){
            
            if(this.transferencia.amount != '' &&  this.transferencia.description != '' && this.transferencia.originAccount != '' && this.transferencia.destinationAccount != ''){
               
                Swal.fire({

                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes!',

                }).then((result) => {
                      
                    if (result.isConfirmed) {
                        Swal.fire(
                            'Successful!',
                            'Successfully related.',
                            'success'
                        )

                        axios.post('/api/clients/current/transactions',`amount=${this.transferencia.amount}&description=${this.transferencia.description}&originAccount=${this.transferencia.originAccount}&destinationAccount=${this.transferencia.destinationAccount}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                        .then(response => {

                            Swal.fire({

                                title: 'Do you want to download the pdf?',
                                text: "download the pdf!!",
                                icon: 'info',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: 'download Pdf',
            
                            }).then((result) => {
                                  
                                if (result.isConfirmed) {

                                    axios.post('/api/transactions/pdf',`numberAccount1=${this.transferencia.originAccount}&numberAccount2=${this.transferencia.destinationAccount}&amount=1500&description=${this.transferencia.destinationAccount}`, {responseType: 'blob'})
                                    .then(response=>{
                                    
                                    let file = response.headers['content-disposition']
                                    let fileName = decodeURI(file.substring(20))
                                    let link = document.createElement('a')
                                    link.href = URL.createObjectURL(response.data)
                                    link.download = fileName
                                    link.click()
                                    link.remove()
                                    Swal.fire(
                                        'Successful!',
                                        'Successfully related.',
                                        'success'
                                    )
                                    setTimeout(location.reload.bind(location), 3000)
                                    })
                                    .catch(error=>  error.response.data)
                                } else {
                                    Swal.fire(
                                        'Successful!',
                                        'Successfully related.',
                                        'success'
                                    )
                                    setTimeout(location.reload.bind(location), 3000)
                                }
                            })    
                                
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops... An error occurred in the transaction',
                              })
                            setTimeout(location.reload.bind(location), 3000)
                            return error.message
                        })
                    } else {
                        setTimeout(location.reload.bind(location), 500)
                    }
                }) 
            } else {
                
                this.checkError = !this.checkError
                Swal.fire({
                    icon: 'error',
                    title: 'Please complete all fields',
                  })
                setTimeout(location.reload.bind(location), 3000)
            }   
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
        desactivarActivarBoton(){
            if( this.transferencia.operationType !== '' && this.transferencia.originAccount !== '' && this.transferencia.destinationAccount  !== '' && this.transferencia.amount!== ''  && this.transferencia.description !== '' ) {

                return false  
                
            } else {

                return true
            }
        },
        accountsDiferent(){
            this.diferentAcoounstTransfer = this.clientAccounts.filter(cuenta => cuenta.number !== this.transferencia.originAccount)
            return this.diferentAcoounstTransfer
        },
    }

})

const verAppVue = app.mount("#app") 