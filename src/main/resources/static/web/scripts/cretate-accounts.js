const app = Vue.createApp({

    data() {
        return {
            clientAccounts: [],
            accounts: [],
            clientLoans: [],
            accountType: '',
        }
    },

    created() {
        
        
        axios.get('/api/clients/current')
        .then(response => {
            this.clientAccounts = response.data
            this.accounts = response.data.accounts.filter( account=> account.accountDelete == false)
            this.clientLoans = response.data.clientLoans
        })
        .catch(error => {
            return error.message
        })

    },
    methods: {

            newAccount(){
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
                    'success',
                  )
                  axios.post('/clients/current/accounts', `accountType=${this.accountType}`)
                  .then(response => { 
                      console.log(' All Ok!!!')
                      window.location.replace("/web/accounts.html")
                  })
                  .catch(error => {
                      
                      console.log(error.response.data)
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
                error.message
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

})

const verAppVue = app.mount("#app") 


