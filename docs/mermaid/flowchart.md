```mermaid
flowchart LR
 subgraph SpringBoot Application
  CTL[Controller]:::orangeBox
  SRV[Service]:::orangeBox
 end
 HDB[H2\nDatabase]:::greenBox
 WBR[Web\nBrowser]

 WBR <--> CTL <--> SRV <--> HDB
 
 classDef greenBox   fill:#00ff00,stroke:#000,stroke-width:3px
 classDef orangeBox  fill:#ffa500,stroke:#000,stroke-width:3px
```