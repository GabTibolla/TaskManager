# API - JAVA 

Projeto inicializado com SpringBoot

# Dependencias
- JPA
- MySql
- WEB

# Conteúdo

Implementação de um sistema de tarefas, onde o mesmo consiste em atribuir tarefas à determinados usuários, que dependendo de sua permissão cadastrada poderão editar, deletar, adicionar atualizações sobre o andamento da tarefa que lhe foi designada. 

# Armazenamento

As informações dos usuarios, das tarefas e das subtarefas são armazenadas no MySQL, onde a configuração deve ser feita em:

*src/main/resources/application.properties*

Aplicando todas as informações necessárias, a comunicação, criação da tabela e configurações das mesmas deve ser feita automaticamente ao rodar o projeto

OBS.: Deve-se criar pelo menos o database e atribuir o nome do mesmo na propriedade *spring.datasource.url*, no caminho indicado acima.
