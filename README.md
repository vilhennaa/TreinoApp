# 🏋️‍♂️ TreinoApp

O **TreinoApp** é um aplicativo Android nativo projetado para simplificar a criação, gestão e execução de rotinas de exercícios. O aplicativo permite que o usuário crie fichas altamente personalizadas e anexe links de vídeos de referência (como YouTube ou Instagram) diretamente aos exercícios, garantindo um guia visual em tempo real para a execução correta dos movimentos.

## ✨ Funcionalidades Principais
* **Gestão de Rotinas:** Criação, edição, visualização e exclusão de fichas de treino (CRUD completo).
* **Modo de Execução de Treino:** Funcionalidade interativa que permite iniciar uma ficha, marcar séries como concluídas, configurar e acompanhar o tempo de descanso via cronômetro, e finalizar o treino.
* **Referências em Vídeo:** Suporte à adição de links de vídeo para cada exercício cadastrado.
* **Interface Reativa:** Sincronização e atualização da interface em tempo real de acordo com as alterações no banco de dados local.

## 🏗️ Arquitetura e Engenharia de Software
O projeto foi estruturado com foco em escalabilidade, manutenibilidade e testes, adotando o padrão **MVVM (Model-View-ViewModel)** aliado a conceitos de **Clean Architecture**:

* **Separation of Concerns (SoC):** A camada de interface (Jetpack Compose) atua de forma passiva, apenas observando e reagindo aos estados emitidos pelo ViewModel, sem armazenar regras de negócio.
* **Observer Pattern:** Utilização de `StateFlow` e `Coroutines` para gerenciar a consistência do estado da UI de forma reativa e assíncrona.
* **Repository Pattern:** Acesso a dados abstraído através da interface `WorkoutRepository`, garantindo o Princípio da Inversão de Dependência (SOLID) e isolando a implementação do banco de dados (Room).
* **Injeção de Dependência:** Desacoplamento de classes e gerenciamento de instâncias (Singletons e ViewModels) realizado através do framework **Koin**.

## 🛠️ Stack Tecnológica
* **Linguagem:** Kotlin
* **UI Declarativa:** Jetpack Compose (Material Design 3)
* **Armazenamento Local:** Room Database com Kotlin Flow
* **Injeção de Dependência:** Koin
* **Navegação:** Jetpack Navigation Compose
* **Qualidade de Software:** JUnit 4 para testes unitários.

## 🧪 Suíte de Testes
O projeto possui uma cobertura de testes unitários focada nas regras de negócio e validações centrais do aplicativo, assegurando a confiabilidade da arquitetura.

Para executar os testes localmente:
1. Abra o projeto no Android Studio.
2. Navegue até o diretório `src/test/java/com/gustavofelipe/treino/ui/createRoutine/`.
3. Clique com o botão direito sobre o arquivo `CreateRoutineViewModelTest.kt` e selecione **"Run"**.


## Link video demostrativo
* https://youtu.be/T4xiPfkzAqQ
