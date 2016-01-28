class Transaction < ApplicationRecord

  belongs_to :bank_account
  belongs_to :payment_mean

  enum transaction_type: [:incoming, :outgoing]

  validates_presence_of :bank_account, :payment_mean, :amount_cents, :amount_currency, :transaction_type, :date, :name

  monetize :amount_cents, with_model_currency: :amount_currency
end
